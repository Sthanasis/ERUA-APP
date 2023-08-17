package gr.erua.service.services;

import gr.erua.service.models.Admin;
import gr.erua.service.models.Problem;
import gr.erua.service.models.Solution;
import gr.erua.service.models.Stakeholder;
import gr.erua.service.repositories.AdminRepository;
import gr.erua.service.repositories.ProblemRepository;
import gr.erua.service.repositories.SolutionRepository;
import gr.erua.service.repositories.StakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    StakeholderRepository stakeholderRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    ProblemRepository problemRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    public void authenticateStakeholder(String email) {
        Stakeholder stakeholder = stakeholderRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Stackeholder with email "+ email +" doesn't exist!"
        ));
        stakeholder.setAuthorized(true);
    }

    @Transactional
    public void deactivateStakeholder(String email) {
        Stakeholder stakeholder = stakeholderRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Stackeholder with email "+ email +" doesn't exist!"
        ));
        stakeholder.setAuthorized(false);
    }

    @Transactional
    public void deleteEruaMemberSolutions(String solutionId){
        Optional<Solution> solution = solutionRepository.findById(Long.valueOf(solutionId));
        if(solution.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Solution with id "+ solutionId +" doesn't exist"
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
        solution.get().getOwner().removeSolution(solution.get());
        solutionRepository.delete(solution.get());
    }

    @Transactional
    public void deleteStackeholderProblem(String problemId){
        Optional<Problem> problem = problemRepository.findById(Long.valueOf(problemId));
        if(problem.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Solution with id "+ problemId +" doesn't exist"
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
        problem.get().getOwner().removeProblem(problem.get());
        problemRepository.delete(problem.get());
    }
}
