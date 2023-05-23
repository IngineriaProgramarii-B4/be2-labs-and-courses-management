package com.example.subject.model;

import com.example.security.objects.DBObject;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "component")
public class Component extends DBObject {
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

    public Component() {
    }

    public Component(String type, int numberWeeks, List<Resource> resources) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.numberWeeks = numberWeeks;
        this.resources = resources;
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

    @Override
    public String toString() {
        return "Component{" +
                "type='" + type + '\'' +
                ", numberWeeks=" + numberWeeks +
                ", resources=" + resources +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
