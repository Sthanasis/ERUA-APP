package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class EruaMember {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String roles;
    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Solution> solutionList;
    @OneToMany
    @JsonIgnore
    private List<EruaMember> eruaMemberList;
    @OneToMany(
            mappedBy = "organizer",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exhibition> exhibitionList;
//    private List<Ideathon> ideathonList;
//    private List<Story> storyList;
//    private List<Document> documentList;

    public EruaMember(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = "ERUA";
    }

    public EruaMember() {
    }

    public void removeExhibition (Exhibition exhibition){
        exhibitionList.remove(exhibition);
    }

    public void addExhibition(Exhibition exhibition){
        exhibition.setOrganizer(this);
        this.exhibitionList.add(exhibition);
    }

    public void addSolution(Solution solution){
        this.solutionList.add(solution);
    }

    public void removeSolution(Solution solution){
        this.solutionList.remove(solution);
    }

    public void addFriend(EruaMember friend){
        this.eruaMemberList.add(friend);
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

    public Set<Solution> getSolutionList() {
        return solutionList;
    }

    public void setSolutionList(Set<Solution> solutionList) {
        this.solutionList = solutionList;
    }

    public List<EruaMember> getEruaMemberList() {
        return eruaMemberList;
    }

    public void setEruaMemberList(List<EruaMember> eruaMemberList) {
        this.eruaMemberList = eruaMemberList;
    }

    public List<Exhibition> getExhibitionList() {
        return exhibitionList;
    }

    public void setExhibitionList(List<Exhibition> exhibitionList) {
        this.exhibitionList = exhibitionList;
    }
}
