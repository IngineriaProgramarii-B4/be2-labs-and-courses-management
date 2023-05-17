package com.example.subject.model;

import com.example.security.objects.DBObject;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "resource")
public class Resource extends DBObject {
    @Id
    @GenericGenerator(
            name = "resource_sequence",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "resource_sequence"
    )
    private UUID id;
    @Column(
            name = "title",
            nullable = false
    )
    private String title;
    @Column(
            name = "location",
            nullable = false,
            unique = true
    )
    private String location;
    @Column(
            name = "type",
            nullable = false
    )
    private String type;

    //constructor


    public Resource() {

    }

    public Resource(String title, String location, String type) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.location = location;
        this.type = type;
    }

    //setters

    public void setTitle(String title) {
        this.title = title;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setType(String type) {
        this.type = type;
    }

    //getters
    public String getTitle() {
        return title;
    }
    public String getLocation() {
        return location;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
