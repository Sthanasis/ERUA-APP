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
public class StakeholderService {

    @Autowired
    EruaMemberRepository eruaMemberRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    StakeholderRepository stakeholderRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Stakeholder> getAllStakeholders(Boolean authorized) {
        if (authorized==null){
            return stakeholderRepository.findAll();
        }
        else {
            return stakeholderRepository.findAllByAuthorized(authorized);
        }
    }

    public Stakeholder createStakeholder(Stakeholder stakeholder) {
        Optional<EruaMember> eruaMember = eruaMemberRepository.findByEmail(stakeholder.getEmail());
        Optional<Admin> admin = adminRepository.findByEmail(stakeholder.getEmail());
        Optional<Stakeholder> member = stakeholderRepository.findByEmail(stakeholder.getEmail());
        if (!member.isPresent() && !eruaMember.isPresent() && !admin.isPresent()){
            stakeholder.setPassword(passwordEncoder.encode(stakeholder.getPassword()));
            stakeholder.setRoles("STAKEHOLDER");
            stakeholder.setAuthorized(false);
            stakeholder.setProblemList(new HashSet<>());
            stakeholderRepository.save(stakeholder);
            return stakeholder;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email is used from another member");
    }

    public ResponseEntity<Map<String, Object>> getAllProblems(String keyword, String contain, int page, int size) {
        List<Problem> problems = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Problem> pageSolutions;
        //get all solutions
        if(keyword==null && contain==null) {
            pageSolutions = problemRepository.findAll(paging);

            problems = pageSolutions.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("solutions", problems);
            response.put("currentPage", pageSolutions.getNumber());
            response.put("totalItems", pageSolutions.getTotalElements());
            response.put("totalPages", pageSolutions.getTotalPages());
        }
        //get all solutions with specific type
        else if(keyword!=null && contain==null){
            pageSolutions = problemRepository.findByKeywordListIgnoreCase(keyword, paging);
        }
        else if(keyword==null && contain!=null){
            //get all solutions that contain something in name
            pageSolutions = problemRepository.findByNameContainingIgnoreCase(contain, paging);

            //get all solutions that contain something in description
            if(pageSolutions.getContent().isEmpty()){
                pageSolutions = problemRepository.findByDescriptionContainingIgnoreCase(contain, paging);
            }
        }
        else {
            //get all solutions with specific type AND contain something in name
            problems = problemRepository.findByNameContainingIgnoreCase(contain)
                    .stream()
                    .filter(s->s.getKeywordList().contains(keyword))
                    .collect(Collectors.toList());
            int start = (int)paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), problems.size());
            pageSolutions = new PageImpl<>(problems.subList(start, end), paging, problems.size());

            //get all solutions with specific type AND contain something in description
            if(problems.isEmpty()){
                problems = problemRepository.findByDescriptionContainingIgnoreCase(contain)
                        .stream()
                        .filter(s->s.getKeywordList().contains(keyword))
                        .collect(Collectors.toList());
                start = (int)paging.getOffset();
                end = Math.min((start + paging.getPageSize()), problems.size());
                pageSolutions = new PageImpl<>(problems.subList(start, end), paging, problems.size());
            }
        }
        problems = pageSolutions.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("solutions", problems);
        response.put("currentPage", pageSolutions.getNumber());
        response.put("totalItems", pageSolutions.getTotalElements());
        response.put("totalPages", pageSolutions.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Set<Problem> getAllProblems(String email) {
        Stakeholder stakeholder = stakeholderRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Stakeholder with email "+ email +" doesn't exist!"
                    );
                });
        return stakeholder.getProblemList();
    }

    public Set<Solution> getSolutionOfProblem(String problemId) {
        Problem problem = problemRepository.findById(Long.valueOf(problemId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                    );
                });
        return problem.getSolutionList();
    }

    public List<Solution> getNotLinkedSolutionsToProblem(String auth_email, String problemId) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        Problem problem = problemRepository.findById(Long.valueOf(problemId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                    );
                });
        List<Solution> solutionList = solutionRepository.findByOwnerEmail(auth_email);

        for (Solution s: problem.getSolutionList()){
            solutionList.remove(s);
        }
        return solutionList;
    }

    @Transactional
    public Set<Problem> createProblem(String auth_email, String email, Problem problem) {
        Stakeholder stakeholder = stakeholderRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Stakeholder with email "+ email +" doesn't exist!"
                    );
                });
        if(!stakeholder.getEmail().equals(auth_email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        problem.setOwner(stakeholder);
        problem.setSolutionList(new HashSet<>());
        stakeholder.addProblem(problem);
        return stakeholder.getProblemList();
    }

    @Transactional
    public void addProblemToSolution(String auth_email, String solutionId, String problemId) {
        //Get member, solution, and problem
        Stakeholder member = stakeholderRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Stakeholder with email "+ auth_email +" doesn't exist!"
                    );
                });
        Problem problem = problemRepository.findById(Long.parseLong(problemId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                    );
                });
        int x= (int) member.getProblemList().stream().filter(s->s.getId().equals(Long.parseLong(problemId))).count();
        if(x==0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't belong to this user!"
            );
        }
        Solution solution = solutionRepository.findById(Long.parseLong(solutionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                    );
                });

        //Add problem to solution
        if(problem.getSolutionList().stream().filter(s->s.getId().equals(solution.getId())).count()==0){
            problem.addSolution(solution);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Solution with id " +solutionId+ " exists in problem with id "+ problemId +"!"
            );
        }
    }

    @Transactional
    public String deleteStackeholderProblem(String auth_email, String problemId) {
        Stakeholder member = stakeholderRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Stackholder with email "+ auth_email +" doesn't exist!"
                    );
                });
        Optional<Problem> problem = member.getProblemList().stream().filter(s -> s.getId().equals(Long.parseLong(problemId))).findFirst();
        if(problem.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist for this user!"
            );
        }

        //ToDo
        // Better performance
        // get problems with this solutionId from problemRepository
        List<Solution> problemList= solutionRepository.findAll();
        for(Solution s: problemList){
            if(s.getProblemList().contains(problem.get())){
                s.removeProblem(problem.get());
            }
        }
        member.removeProblem(problem.get());
        problemRepository.delete(problem.get());
        return "Done!";
    }

    @Transactional
    public void removeSolutionFromProblem(String auth_email, String solutionId, String problemId) {
        Optional<Stakeholder> memberSt = stakeholderRepository.findByEmail(auth_email);
        if(memberSt.isPresent()){
            //find problem
            Problem problem = problemRepository.findById(Long.parseLong(problemId))
                    .orElseThrow(() -> {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                        );
                    });
            if(!memberSt.get().getProblemList().contains(problem)){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist for this Stakeholder!"
                );
            }
            //find solution
            Solution solution = solutionRepository.findById(Long.parseLong(solutionId))
                    .orElseThrow(() -> {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                        );
                    });
            if(!problem.getSolutionList().contains(solution)){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't have Solution with id " + solutionId + "!"
                );
            }

            //remove connection
            problem.removeSolution(solution);
        }
        else {
            Optional<EruaMember> memberEr = eruaMemberRepository.findByEmail(auth_email);
            if(memberEr.isPresent()){
                //find solution
                Solution solution = solutionRepository.findById(Long.parseLong(solutionId))
                        .orElseThrow(() -> {
                            throw new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist!"
                            );
                        });
                if(!memberEr.get().getSolutionList().contains(solution)){
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ solution +" doesn't exist for this Stakeholder!"
                    );
                }
                //find problem
                Problem problem = problemRepository.findById(Long.parseLong(problemId))
                        .orElseThrow(() -> {
                            throw new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't exist!"
                            );
                        });
                if(!problem.getSolutionList().contains(solution)){
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Problem with id "+ problemId +" doesn't have Solution with id " + solutionId + "!"
                    );
                }

                //remove connection
                problem.removeSolution(solution);
            }
//            else {
//                User is Admin
//            }
        }
    }

}
