package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private EruaMember owner;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String typeOfSolution;
    @ManyToMany(mappedBy="solutionList")
    @JsonIgnore
    private Set<Problem> problemList;

    public Solution(String name, String description, String typeOfSolution) {
        this.name = name;
        this.description = description;
        this.typeOfSolution = typeOfSolution;
        problemList = new HashSet<>();
    }

    public Solution() {
    }

    public void addProblem(Problem problem){
        this.problemList.add(problem);
    }

    public void removeProblem(Problem problem){
        this.problemList.remove(problem);
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

    public EruaMember getOwner() {
        return owner;
    }

    public void setOwner(EruaMember owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfSolution() {
        return typeOfSolution;
    }

    public void setTypeOfSolution(String typeOfSolution) {
        this.typeOfSolution = typeOfSolution;
    }

    public Set<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(Set<Problem> problemList) {
        this.problemList = problemList;
    }
}
