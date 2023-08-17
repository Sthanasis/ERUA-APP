package gr.erua.service.controllers;

import gr.erua.service.models.Admin;
import gr.erua.service.services.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping
    List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("/authenticateStakeholder")
    void authenticateStakeholder(@RequestParam String email){
        adminService.authenticateStakeholder(email);
    }

    @GetMapping("/deactivateStakeholder")
    void deactivateStakeholder(@RequestParam String email){
        adminService.deactivateStakeholder(email);
    }

    @DeleteMapping("/solution")
    void deleteEruaMemberSolutions(@RequestParam String solutionId){
        adminService.deleteEruaMemberSolutions(solutionId);
    }

    @DeleteMapping("/problem")
    void deleteStackeholderProblem(@RequestParam String problemId){
        adminService.deleteStackeholderProblem(problemId);
    }
}
