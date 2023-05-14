package com.example.subject.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resource")
public class Resource {
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
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(
            name = "type",
            nullable = false
    )
    private String type;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    //constructor


    public Resource() {

    }

    public Resource(String title, String location, String type, boolean isDeleted) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.location = location;
        this.type = type;
        this.isDeleted = isDeleted;
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
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
