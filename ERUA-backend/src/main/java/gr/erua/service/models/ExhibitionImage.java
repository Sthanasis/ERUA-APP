package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ExhibitionImage {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @JsonIgnore
    private String path;
    @ManyToOne
    @JsonIgnore
    private Exhibition exhibition;
    @ManyToOne
    private EruaMember owner;

    public ExhibitionImage(String name, String path, EruaMember owner) {
        this.name = name;
        this.path = path;
        this.owner = owner;
    }

    public ExhibitionImage(){}

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public EruaMember getOwner() {
        return owner;
    }

    public void setOwner(EruaMember owner) {
        this.owner = owner;
    }
}
