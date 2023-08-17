package gr.erua.service.services;

import gr.erua.service.models.Admin;
import gr.erua.service.models.EruaMember;
import gr.erua.service.models.SecurityUser;
import gr.erua.service.models.Stakeholder;
import gr.erua.service.repositories.AdminRepository;
import gr.erua.service.repositories.EruaMemberRepository;
import gr.erua.service.repositories.StakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    EruaMemberRepository eruaMemberRepository;
    @Autowired
    StakeholderRepository stakeholderRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin= adminRepository.findByEmail(username);
        if(admin.isPresent()) {
            return new SecurityUser(admin.get());
        }
        Optional<EruaMember> eruaMember= eruaMemberRepository.findByEmail(username);
        if(eruaMember.isPresent()) {
            return new SecurityUser(eruaMember.get());
        }
        Optional<Stakeholder> stakeholder= stakeholderRepository.findByEmail(username);
        if(stakeholder.isPresent()) {
            return new SecurityUser(stakeholder.get());
        }

        throw new UsernameNotFoundException("Email not found: " + username);
    }
}