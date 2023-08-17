package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    private Stakeholder owner;
    @ElementCollection
    private List<String> keywordList;
    @ManyToMany
    @JoinTable(name="problem_solutions",
            joinColumns = @JoinColumn(name="problem_name"),
            inverseJoinColumns = @JoinColumn(name="solution_name"))
    @JsonIgnore
    private Set<Solution> solutionList;

    public Problem(String name, String description, List<String> keywords) {
        this.name = name;
        this.description = description;
        this.keywordList = keywords;
        solutionList = new HashSet<>();
    }

    public Problem() {
    }

    public void addSolution(Solution solution){
        this.solutionList.add(solution);
        solution.addProblem(this);
    }

    public void removeSolution(Solution solution){
        this.solutionList.remove(solution);
        solution.removeProblem(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Stakeholder getOwner() {
        return owner;
    }

    public void setOwner(Stakeholder owner) {
        this.owner = owner;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    public Set<Solution> getSolutionList() {
        return solutionList;
    }

    public void setSolutionList(Set<Solution> solutionList) {
        this.solutionList = solutionList;
    }
}
