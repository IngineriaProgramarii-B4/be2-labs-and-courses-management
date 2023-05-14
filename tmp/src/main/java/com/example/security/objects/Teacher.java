package com.example.security.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Teacher extends AcademicStaff {

    private Set<String> taughtSubjects = new HashSet<>();
    private String title;

    public Teacher() {

    }

//    public Teacher(UUID id, String firstname, String lastname, String email, String username, String office, Set<String> teachedSubjects, String title) {
//        super(id, firstname, lastname, email, username, office, 1);
//        this.taughtSubjects = teachedSubjects;
//        this.title = title;
//    }
    public Teacher(String firstname, String lastname, String email, String username, String office,String registrationNumber, Set<String> teachedSubjects, String title) {
        super(firstname, lastname, email, username, office, 2,registrationNumber);
        this.taughtSubjects = teachedSubjects;
        this.title = title;
    }

    public Set<String> getTaughtSubjects() {
        return taughtSubjects;
    }

    public void setTaughtSubjects(Set<String> taughtSubjects) {
        this.taughtSubjects = taughtSubjects;
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
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", type=" + type +
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