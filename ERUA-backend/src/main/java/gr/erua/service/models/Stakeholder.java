package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Stakeholder {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String roles;
    private Boolean authorized;
    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Problem> problemList;
//    private List<Document> documentList;

    public Stakeholder(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorized = false;
        this.roles = "STAKEHOLDER";
    }

    public Stakeholder() {
    }

    public void addProblem(Problem problem){
        this.problemList.add(problem);
    }

    public void removeProblem(Problem problem){
        this.problemList.remove(problem);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public Set<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(Set<Problem> problemList) {
        this.problemList = problemList;
    }
}
