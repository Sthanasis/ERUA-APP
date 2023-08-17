package gr.erua.service.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.erua.service.models.Problem;
import gr.erua.service.models.Solution;
import gr.erua.service.models.Stakeholder;
import gr.erua.service.services.StakeholderService;
import gr.erua.service.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/stakeholder")
public class StakeholderController {

    @Autowired
    StakeholderService stakeholderService;

    @PostMapping
    Stakeholder createStakeholder(@RequestBody Stakeholder stakeholder){
        return stakeholderService.createStakeholder(stakeholder);
    }

    @GetMapping
    List<Stakeholder> getAllStakeholders(@RequestParam(required = false) Boolean authorized){
        return stakeholderService.getAllStakeholders(authorized);
    }

    @GetMapping("/problems")
    ResponseEntity<Map<String, Object>> getAllProblems(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) String contain,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "100") int size){
        return stakeholderService.getAllProblems(keyword,contain,page,size);
    }

    @DeleteMapping("/problems")
    String deleteStackeholderProblem(HttpServletRequest request, @RequestParam String problemId){
        DecodedJWT decodedJWT= TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return stakeholderService.deleteStackeholderProblem(decodedJWT.getSubject(), problemId);
    }

    @GetMapping("/problems/{problemId}/solutions")
    Set<Solution> getSolutionOfProblem(@PathVariable String problemId){
        return stakeholderService.getSolutionOfProblem(problemId);
    }

    @GetMapping("/problems/notlinkedsolutions")
    List<Solution> getNotLinkedSolutionsToProblem(HttpServletRequest request, @RequestParam String problemId){
        DecodedJWT decodedJWT= TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return stakeholderService.getNotLinkedSolutionsToProblem(decodedJWT.getSubject(), problemId);
    }

    @GetMapping("/{email}/problems")
    Set<Problem> getAllProblems(@PathVariable String email){
        return stakeholderService.getAllProblems(email);
    }

    @PostMapping("/{email}/problems")
    Set<Problem> createProblem(HttpServletRequest request, @PathVariable String email, @RequestBody Problem problem){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return stakeholderService.createProblem(decodedJWT.getSubject(), email, problem);
    }

    @GetMapping("/addproblemtosolution")
    void addProblemToSolution(HttpServletRequest request, @RequestParam String solutionId, @RequestParam String problemId){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        stakeholderService.addProblemToSolution(decodedJWT.getSubject(), solutionId, problemId);
    }

    @DeleteMapping("/removesolutionfromproblem")
    void removeSolutionFromProblem(HttpServletRequest request, @RequestParam String solutionId, @RequestParam String problemId){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        stakeholderService.removeSolutionFromProblem(decodedJWT.getSubject(), solutionId, problemId);
    }
}
