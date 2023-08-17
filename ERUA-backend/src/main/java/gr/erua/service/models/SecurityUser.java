package gr.erua.service.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {

    private Admin userAdmin=null;
    private EruaMember eruaMember=null;
    private Stakeholder stakeholder=null;

    public SecurityUser(Admin user) {
        this.userAdmin = user;
    }
    public SecurityUser(EruaMember user) {
        this.eruaMember = user;
    }
    public SecurityUser(Stakeholder user) {
        this.stakeholder = user;
    }

    public String getName() {
        if(userAdmin!=null) {
            return userAdmin.getName();
        } else if (eruaMember!=null) {
            return eruaMember.getName();
        } else if (stakeholder!=null) {
            return stakeholder.getName();
        }
        return "";
    }

    public String getId() {
        if(userAdmin!=null) {
            return userAdmin.getId().toString();
        } else if (eruaMember!=null) {
            return eruaMember.getId().toString();
        } else if (stakeholder!=null) {
            return stakeholder.getId().toString();
        }
        return "";
    }

    /**
     * The username is actually the email in our case
     * @return
     */
    @Override
    public String getUsername() {
        if(userAdmin!=null) {
            return userAdmin.getEmail();
        } else if (eruaMember!=null) {
            return eruaMember.getEmail();
        } else if (stakeholder!=null) {
            return stakeholder.getEmail();
        }
        return "";
    }

    @Override
    public String getPassword() {
        if(userAdmin!=null) {
            return userAdmin.getPassword();
        } else if (eruaMember!=null) {
            return eruaMember.getPassword();
        } else if (stakeholder!=null) {
            return stakeholder.getPassword();
        }
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userAdmin!=null) {
            return Arrays.stream(userAdmin
                            .getRoles()
                            .split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (eruaMember!=null) {
            return Arrays.stream(eruaMember
                            .getRoles()
                            .split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (stakeholder!=null) {
            return Arrays.stream(stakeholder
                            .getRoles()
                            .split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
        
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (stakeholder!=null) {
            return stakeholder.getAuthorized();
        }
        return true;
    }
}