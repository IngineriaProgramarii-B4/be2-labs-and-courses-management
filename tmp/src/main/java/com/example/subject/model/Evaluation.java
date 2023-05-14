package com.example.subject.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GenericGenerator(
            name = "evaluation_sequence",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "evaluation_sequence"
    )
    private UUID id;
    @Column(name = "component", nullable = false)
    private String component;
    @Column(name = "value", nullable = false)
    private float value;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Evaluation() {
    }

    public Evaluation(String component, float value, String description, boolean isDeleted) {
        this.id = UUID.randomUUID();
        this.component = component;
        this.value = value;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public UUID getId() {
        return id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "component='" + component + '\'' +
                ", value=" + value +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
