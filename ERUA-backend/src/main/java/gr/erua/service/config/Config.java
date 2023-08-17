package gr.erua.service.config;

import gr.erua.service.models.*;
import gr.erua.service.repositories.AdminRepository;
import gr.erua.service.repositories.EruaMemberRepository;
import gr.erua.service.repositories.ProblemRepository;
import gr.erua.service.repositories.StakeholderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(EruaMemberRepository eruaMemberRepository,
                                        AdminRepository adminRepository,
                                        StakeholderRepository stakeholderRepository,
                                        ProblemRepository problemRepository,
                                        PasswordEncoder passwordEncoder){
        return args -> {
            //Create dummy users
            Admin admin = new Admin("admin","admin@gr",passwordEncoder.encode("admin"));
            adminRepository.save(admin);

            EruaMember eruaMember1 = new EruaMember("Nikos Nik","nikos@gr",passwordEncoder.encode("nikos"));
            eruaMemberRepository.save(eruaMember1);

            EruaMember eruaMember2 = new EruaMember("Sakis Sim","sakis@gr",passwordEncoder.encode("sakis"));

            Stakeholder stakeholder1 = new Stakeholder("Pfizer Thessaloniki","pfizer@gr",passwordEncoder.encode("pfizer"));
            stakeholderRepository.save(stakeholder1);

            Stakeholder stakeholder2 = new Stakeholder("PWC Thessaloniki","pwc@gr",passwordEncoder.encode("pwc"));
            stakeholder2.setAuthorized(true);


            //Create dummy problem
            stakeholder2.setProblemList(new HashSet<>());
            Problem problem1 = new Problem("Website","Website for Bank", List.of(new String[]{"frontend", "react"}));
            problem1.setOwner(stakeholder2);
            stakeholder2.addProblem(problem1);

            Problem problem2 = new Problem("Service","Backend service for Bank", List.of(new String[]{"backend", "java"}));
            problem2.setOwner(stakeholder2);
            stakeholder2.addProblem(problem2);


            //Create dummy solution
            eruaMember2.setSolutionList(new HashSet<>());
            Solution solution = new Solution("Website Development","Frontend website development","coding");
            solution.setOwner(eruaMember2);
            eruaMember2.addSolution(solution);

            Solution solution2 = new Solution("Backend Development","Backend development","coding");
            solution2.setOwner(eruaMember2);
            eruaMember2.addSolution(solution2);

            problem1.addSolution(solution);
            problem1.addSolution(solution2);


            eruaMemberRepository.save(eruaMember2);
            stakeholderRepository.save(stakeholder2);
        };
    }
}
