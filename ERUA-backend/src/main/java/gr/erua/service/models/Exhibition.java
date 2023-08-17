package gr.erua.service.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Exhibition {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy")
    public Date eventDate;
    @ManyToOne
    private EruaMember organizer;
    @OneToMany(
            mappedBy = "exhibition",
            cascade = CascadeType.ALL)
    private List<ExhibitionImage> exhibitionImageList;

    public Exhibition(String name, String description, Date eventDate){
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
    }

    public Exhibition(){}

    public void addImagetoList(ExhibitionImage exhibitionImage){
        exhibitionImage.setExhibition(this);
        this.exhibitionImageList.add(exhibitionImage);
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

    public EruaMember getOrganizer() {
        return organizer;
    }

    public void setOrganizer(EruaMember organizer) {
        this.organizer = organizer;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public List<ExhibitionImage> getExhibitionImageList() {
        return exhibitionImageList;
    }

    public void setExhibitionImageList(List<ExhibitionImage> exhibitionImageList) {
        this.exhibitionImageList = exhibitionImageList;
    }
}
