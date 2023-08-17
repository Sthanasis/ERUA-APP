package gr.erua.service.services;

import gr.erua.service.models.*;
import gr.erua.service.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EruaMemberService {

    @Autowired
    EruaMemberRepository eruaMemberRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    StakeholderRepository stakeholderRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public EruaMember createEruaMember(EruaMember eruaMember) {
        Optional<EruaMember> member = eruaMemberRepository.findByEmail(eruaMember.getEmail());
        Optional<Admin> admin = adminRepository.findByEmail(eruaMember.getEmail());
        Optional<Stakeholder> stakeholder = stakeholderRepository.findByEmail(eruaMember.getEmail());
        if (!member.isPresent() && !admin.isPresent() && !stakeholder.isPresent()){
            eruaMember.setPassword(passwordEncoder.encode(eruaMember.getPassword()));
            eruaMember.setRoles("ERUA");
            eruaMember.setSolutionList(new HashSet<>());
            eruaMember.setEruaMemberList(new ArrayList<>());
            eruaMemberRepository.save(eruaMember);
            return eruaMember;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email is used from another member");
    }

    public List<EruaMember> getAllEruaMembers() {
        return eruaMemberRepository.findAll();
    }

    public EruaMember getEruaMember(String id) {
        return eruaMemberRepository.findById(UUID.fromString(id)).orElseThrow(() ->{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "EruaMember with id "+ id +" doesn't exist!"
            );
        });
    }

    public EruaMember getEruaMemberWithEmail(String email) {
        return eruaMemberRepository.findByEmail(email).orElseThrow(() ->{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "EruaMember with email "+ email +" doesn't exist!"
            );
        });
    }

    /**
     * Get all Solution of all eruamembers
     *  Two filters exist for the type and the containing value
     * @param type for specific type of solution
     * @param contain for value to exist in title or at least in description
     * @param page
     * @param size
     */
    public ResponseEntity<Map<String, Object>> getAllSolutions(String type, String contain, int page, int size) {
        List<Solution> solutions = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Solution> pageSolutions;
        //get all solutions
        if(type==null && contain==null) {
            pageSolutions = solutionRepository.findAll(paging);

            solutions = pageSolutions.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("solutions", solutions);
            response.put("currentPage", pageSolutions.getNumber());
            response.put("totalItems", pageSolutions.getTotalElements());
            response.put("totalPages", pageSolutions.getTotalPages());
        }
        //get all solutions with specific type
        else if(type!=null && contain==null){
            pageSolutions = solutionRepository.findByTypeOfSolutionIgnoreCase(type, paging);
        }
        else if(type==null && contain!=null){
            //get all solutions that contain something in name
            pageSolutions = solutionRepository.findByNameContainingIgnoreCase(contain, paging);

            //get all solutions that contain something in description
            if(pageSolutions.getContent().isEmpty()){
                pageSolutions = solutionRepository.findByDescriptionContainingIgnoreCase(contain, paging);
            }
        }
        else {
            //get all solutions with specific type AND contain something in name
            solutions = solutionRepository.findByNameContainingIgnoreCase(contain)
                    .stream()
                    .filter(s->s.getTypeOfSolution().equals(type))
                    .collect(Collectors.toList());
            int start = (int)paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), solutions.size());
            pageSolutions = new PageImpl<>(solutions.subList(start, end), paging, solutions.size());

            //get all solutions with specific type AND contain something in description
            if(solutions.isEmpty()){
                solutions = solutionRepository.findByDescriptionContainingIgnoreCase(contain)
                        .stream()
                        .filter(s->s.getTypeOfSolution().equals(type))
                        .collect(Collectors.toList());
                start = (int)paging.getOffset();
                end = Math.min((start + paging.getPageSize()), solutions.size());
                pageSolutions = new PageImpl<>(solutions.subList(start, end), paging, solutions.size());
            }
        }

        solutions = pageSolutions.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("solutions", solutions);
        response.put("currentPage", pageSolutions.getNumber());
        response.put("totalItems", pageSolutions.getTotalElements());
        response.put("totalPages", pageSolutions.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all the unique type of solution that exist
     * @return
     */
    public List<String> getAllSolutionsTypes() {
        return solutionRepository.getDistinctTypes();
    }

    public Set<Solution> getEruaMemberSolutions(String email) {
        EruaMember member = eruaMemberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ email +" doesn't exist!"
                    );
                });
        return member.getSolutionList();
    }

    @Transactional
    public Set<Solution> createEruaMemberSolutions(String auth_email, String email, Solution solution) {
        EruaMember member = eruaMemberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ email +" doesn't exist!"
                    );
                });
        if(!member.getEmail().equals(auth_email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if(member.getSolutionList().stream().anyMatch(s->s.getName().equals(solution.getName()))){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "EruaMember has solution with name "+ solution.getName() +"!"
            );
        }
        solution.setOwner(member);
        member.addSolution(solution);
        return member.getSolutionList();
    }

    @Transactional
    public String deleteEruaMemberSolutions(String auth_email, String solutionId) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        Optional<Solution> solution = member.getSolutionList().stream().filter(s -> s.getId().equals(Long.parseLong(solutionId))).findFirst();
        if(solution.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist for this user!"
            );
        }

        //ToDo
        // Better performance
        // get problems with this solutionId from problemRepository
        List<Problem> problemList= problemRepository.findAll();
        for(Problem p: problemList){
            if(p.getSolutionList().contains(solution.get())){
                p.removeSolution(solution.get());
            }
        }
        member.removeSolution(solution.get());
        solutionRepository.delete(solution.get());
        return "Done!";
    }

    public List<EruaMember> getEruaMemberFriends(String email) {
        EruaMember member = eruaMemberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ email +" doesn't exist!"
                    );
                });
        return member.getEruaMemberList();
    }

    @Transactional
    public List<EruaMember> createEruaMemberFriends(String auth_email, String email, String friend_email) {
        EruaMember member1 = eruaMemberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ email +" doesn't exist!"
                    );
                });
        if(!member1.getEmail().equals(auth_email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        EruaMember member2 = eruaMemberRepository.findByEmail(friend_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "EruaMember with email "+ friend_email +" doesn't exist!"
                    );
                });
        member1.addFriend(member2);
        return member1.getEruaMemberList();
    }

    @Transactional
    public void addSolutionToProblem(String auth_email, String solutionId, String problemId) {
        //Get member, solution, and problem
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        Solution solution = solutionRepository.findById(Long.parseLong(solutionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                    );
                });
        int x= (int) member.getSolutionList().stream().filter(s->s.getId().equals(Long.parseLong(solutionId))).count();
        if(x==0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't belong to this user!"
            );
        }
        Problem problem = problemRepository.findById(Long.parseLong(problemId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                    );
                });

        //Add solution to problem
        if(problem.getSolutionList().stream().filter(s->s.getId().equals(solution.getId())).count()==0){
            problem.addSolution(solution);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Solution with id " +solutionId+ " exists in problem with id "+ problemId +"!"
            );
        }
    }

    public Set<Problem> getProblemsOfSolution(String solutionId) {
        Solution solution = solutionRepository.findById(Long.valueOf(solutionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                    );
                });
        return solution.getProblemList();
    }

    public List<Problem> getNotLinkedProblemsToSolution(String auth_email, String solutionId) {
        Stakeholder stakeholder = stakeholderRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Stakeholder with email "+ auth_email +" doesn't exist!"
                    );
                });
        Solution solution = solutionRepository.findById(Long.valueOf(solutionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                    );
                });
        List<Problem> problemList = problemRepository.findByOwnerEmail(auth_email);

        for (Problem p: solution.getProblemList()){
            problemList.remove(p);
        }
        return problemList;
    }
}
