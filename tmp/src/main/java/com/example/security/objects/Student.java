package com.example.security.objects;

import com.example.catalog.models.Grade;
import com.example.subject.model.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.*;

@Entity
@SQLDelete(sql="UPDATE Student SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "students")
public class Student extends User {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "enrolled_courses",
            joinColumns = @JoinColumn(name = "students_id"),
            inverseJoinColumns = @JoinColumn(name = "courses_id")
    )
    private Set<Subject> enrolledCourses = new HashSet<>();
//    @Min(value=1)
//    @Max(value=3)
    private int year;
//    @Min(value=1)
//    @Max(value=6)
    private int semester;
    private String grupa;

    // <-------------------------------- FROM CATALOG ----------------------------------> //
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Grade> grades = new ArrayList<>();
    // <--------------------------------------------------------------------------------> //

    public Student(UUID id,
                   String firstname,
                   String lastname,
                   String email,
                   String username,
                   int year,
                   int semester,
                   String registrationNumber,
                   Set<Subject> enrolledCourses) {
        super(id, firstname, lastname, email, username);
        this.enrolledCourses = enrolledCourses;
        this.year = year;
        this.semester = semester;
        this.registrationNumber = registrationNumber;
    }

    public Student(String firstname,
                   String lastname,
                   String email,
                   String username,
                   int year,
                   int semester,
                   String registrationNumber,
                   Set<Subject> enrolledCourses) {
        super(firstname, lastname, email, username, registrationNumber);
        this.enrolledCourses = enrolledCourses;
        this.year = year;
        this.semester = semester;
        this.registrationNumber = registrationNumber;
    }

    public Student() {

    }

    public void setEnrolledCourses(Set<Subject> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Set<Subject> getEnrolledCourses() {
        return enrolledCourses;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
    public void addEnrolledCourse(Subject course) {
        enrolledCourses.add(course);
    }

    public String getGrupa() {
        return grupa;
    }




    @Override
    public String toString() {
        return "Student{" +
                "enrolledCourses=" + enrolledCourses +
                ", year=" + year +
                ", semester=" + semester +
                ", grupa='" + grupa + '\'' +
                ", grades=" + grades +
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

    // <-------------------------------- FROM CATALOG ----------------------------------> //

    // ati putea face add si set comun si in if-else doar sa modificati maxGradeId
    public void addGrade(Grade grade) {
        grades.add(grade);
        grade.setId(UUID.randomUUID());
    }

    public List<Grade> getGrades() {
        List<Grade> gradesList = new ArrayList<>();
        for (Grade grade : this.grades) {
            if (!grade.isDeleted()) {
                gradesList.add(grade);
            }
        }
        return gradesList;
    }

    public Grade getGradeById(UUID id) {
        for (Grade grade : this.getGrades()) {
            if (grade.getId() == id) {
                return grade;
            }
        }
        return null;
    }
    // ** //
    public List<Grade> getGradesBySubject(String subject) {
        List<Grade> gradesList = new ArrayList<>();
        for (Grade grade : this.getGrades()) {
            if (grade.getSubject().getTitle().equals(subject)) {
                gradesList.add(grade);
            }
        }
        return gradesList;
    }
}