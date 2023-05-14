package com.example.subject.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "component")
public class Component {
    @Id
    @GenericGenerator(
            name = "component_sequence",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "component_sequence"
    )
    private UUID id;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "number_weeks", nullable = false)
    private int numberWeeks;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private List<Resource> resources = new ArrayList<>();
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Component() {
    }

    public Component(String type, int numberWeeks, List<Resource> resources, boolean isDeleted) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.numberWeeks = numberWeeks;
        this.resources = resources;
        this.isDeleted = isDeleted;
    }

    //setters
    public void setType(String type) {
        this.type = type;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void setNumberWeeks(int numberWeeks) {
        this.numberWeeks = numberWeeks;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    //getters

    public String getType() {
        return type;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public int getNumberWeeks() {
        return numberWeeks;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    public void removeResource(Resource resource) {
        this.resources.remove(resource);
    }

    public void softDeleteResource(Resource resource) {
        int index = resources.indexOf(resource);
        if (index != -1) {
            resource.setDeleted(true);
            resources.set(index, resource);
        }
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Component{" +
                "type='" + type + '\'' +
                ", numberWeeks=" + numberWeeks +
                ", resources=" + resources +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
