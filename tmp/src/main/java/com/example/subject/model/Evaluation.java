package com.example.subject.model;

import com.example.security.objects.DBObject;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "evaluation")
public class Evaluation extends DBObject {
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

    public Evaluation() {
    }

    public Evaluation(String component, float value, String description) {
        this.id = UUID.randomUUID();
        this.component = component;
        this.value = value;
        this.description = description;
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

    @Override
    public String toString() {
        return "Evaluation{" +
                "component='" + component + '\'' +
                ", value=" + value +
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
