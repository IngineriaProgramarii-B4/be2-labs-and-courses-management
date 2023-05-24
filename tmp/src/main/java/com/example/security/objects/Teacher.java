package com.example.security.objects;

import com.example.subject.model.Subject;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Teacher extends AcademicStaff {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "taught_subjects",
            joinColumns = @JoinColumn(name = "teachers_id"),
            inverseJoinColumns = @JoinColumn(name = "courses_id")
    )
    private Set<Subject> taughtSubjects = new HashSet<>();
    private String title;

    public Teacher() {

    }

    public Teacher(UUID id, String firstname, String lastname, String email, String username, String office, Set<Subject> taughtSubjects, String title, String registrationNumber) {
        super(id, firstname, lastname, email, username, office, registrationNumber);
        this.taughtSubjects = taughtSubjects;
        this.title = title;
    }
    public Teacher(String firstname, String lastname, String email, String username, String office, String registrationNumber, Set<Subject> taughtSubjects, String title) {
        super(firstname, lastname, email, username, office, registrationNumber);
        this.taughtSubjects = taughtSubjects;
        this.title = title;
    }

    public void setTaughtSubjects(Set<Subject> taughtSubjects) {
        this.taughtSubjects = taughtSubjects;
    }

    public Set<Subject> getTaughtSubjects() {
        return taughtSubjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "taughtSubjects=" + taughtSubjects +
                ", title='" + title + '\'' +
                ", office='" + office + '\'' +
                ", id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object user) {
        return super.equals(user);
    }
}