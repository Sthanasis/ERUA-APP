package gr.erua.service.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.erua.service.controllers.entities.ResponseBasicUser;
import gr.erua.service.models.Admin;
import gr.erua.service.models.EruaMember;
import gr.erua.service.models.Stakeholder;
import gr.erua.service.repositories.AdminRepository;
import gr.erua.service.repositories.EruaMemberRepository;
import gr.erua.service.repositories.StakeholderRepository;
import gr.erua.service.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/user")
public class BasicController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    EruaMemberRepository eruaMemberRepository;

    @Autowired
    StakeholderRepository stakeholderRepository;

    @GetMapping
    ResponseBasicUser getUserWithEmail(@RequestParam(required = true) String email) {
        ResponseBasicUser responseBasicUser;
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if(admin.isPresent()){
            responseBasicUser = new ResponseBasicUser(admin.get().getName(), admin.get().getEmail(), admin.get().getRoles());
        }
        else {
            Optional<EruaMember> eruaMember = eruaMemberRepository.findByEmail(email);
            if(eruaMember.isPresent()){
                responseBasicUser = new ResponseBasicUser(eruaMember.get().getName(), eruaMember.get().getEmail(), eruaMember.get().getRoles());
            }
            else {
                Optional<Stakeholder> stakeholder = stakeholderRepository.findByEmail(email);
                if(stakeholder.isPresent()){
                    responseBasicUser = new ResponseBasicUser(stakeholder.get().getName(), stakeholder.get().getEmail(), stakeholder.get().getRoles());
                }
                else {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Member with email "+ email +" doesn't exist!"
                    );
                }
            }
        }

        return responseBasicUser;
    }

    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(authorizationHeader);
                String email = decodedJWT.getSubject();

                //get user in order to get roles
                List<String> roles =new ArrayList<>();
                String name="";
                String id="";
                Optional<Admin> admin = adminRepository.findByEmail(email);
                if (admin.isPresent()){
                    roles= stream(admin.get().getRoles().split(",")).collect(Collectors.toList());
                    name= admin.get().getName();
                    id= admin.get().getId().toString();
                }
                else {
                    Optional<EruaMember> eruaMember = eruaMemberRepository.findByEmail(email);
                    if(eruaMember.isPresent()){
                        roles= stream(eruaMember.get().getRoles().split(",")).collect(Collectors.toList());
                        name= eruaMember.get().getName();
                        id= eruaMember.get().getId().toString();
                    }
                    else {
                        Optional<Stakeholder> stakeholder = stakeholderRepository.findByEmail(email);
                        if(stakeholder.isPresent()){
                            roles= stream(stakeholder.get().getRoles().split(",")).collect(Collectors.toList());
                            name= stakeholder.get().getName();
                            id= stakeholder.get().getId().toString();
                        }
                    }
                }

                //Generate tokens
                String accessToken = TokenUtil.generateAccessToken(email, request.getRequestURL(), id, name, roles);
                String refreshToken = TokenUtil.generateRefreshToken(email, request.getRequestURL());

                Map<String,String> tokens= new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
            catch (Exception e){
                System.err.println("Error with token: "+e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error= new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
