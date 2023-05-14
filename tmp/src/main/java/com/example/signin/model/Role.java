package com.example.signin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Override
    public String toString() {
        return "Role(id=" + id + ", name=" + name + ")";
    }
}
