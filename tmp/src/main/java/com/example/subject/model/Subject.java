
package com.example.subject.model;

import com.example.security.objects.DBObject;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Entity
@Table(name = "subject")
public class Subject extends DBObject {
    @Id
    @GenericGenerator(
            name = "subject_sequence",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "subject_sequence"
    )
    private UUID id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "credits", nullable = false)
    private int credits;
    @Column(name = "year", nullable = false)
    private int year;
    @Column(name = "semester", nullable = false)
    private int semester;
    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;
    // describe a One-to-Many relationship between Subject and Approfundation
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private List<Component> componentList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private List<Evaluation> evaluationList = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Resource image;

    //constructors
    public Subject() {
    }

    public Subject(String title, int credits, int year, int semester, String description, List<Component> componentList,
                   List<Evaluation> evaluationList) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.credits = credits;
        this.year = year;
        this.semester = semester;
        this.description = description;
        this.componentList = componentList;
        this.evaluationList = evaluationList;
    }

    //setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setEvaluationList(List<Evaluation> evaluationList) {
        this.evaluationList = evaluationList;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setDescription(String description) { this.description = description; }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public Resource getImage() {
        return image;
    }

    public void setImage(Resource image) {
        this.image = image;
    }

    //getters
    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public List<Evaluation> getEvaluationList() {
        return evaluationList;
    }

    public int getYear() {
        return year;
    }

    public int getSemester() {
        return semester;
    }

    public String getDescription() { return description;}

    public List<Component> getComponentList() {
        return componentList;
    }

    //additional methods

    public void addComponent(Component component) {
        componentList.add(component);
    }

    public void removeComponent(Component component) {
        componentList.remove(component);
    }

    public void softDeleteComponent(Component component) {
        int index = componentList.indexOf(component);
        if (index != -1) {
            component.setDeleted(true);
            componentList.set(index, component);
        }
    }

    public void addEvaluation(Evaluation evaluation) {
        evaluationList.add(evaluation);
    }

    public void removeEvaluation(Evaluation evaluation) {
        evaluationList.remove(evaluation);
    }

    public void softDeleteEvaluation(Evaluation evaluation) {
        int index = evaluationList.indexOf(evaluation);
        if (index != -1) {
            evaluation.setDeleted(true);
            evaluationList.set(index, evaluation);
        }
    }

    @Override
    public String toString() {
        return "Subject{" +
                "title='" + title + '\'' +
                ", credits=" + credits +
                ", year=" + year +
                ", semester=" + semester +
                ", description='" + description + '\'' +
                ", componentList=" + componentList +
                ", evaluationList=" + evaluationList +
                ", image=" + image +
                '}';
    }
}