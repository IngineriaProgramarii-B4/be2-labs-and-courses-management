package com.example.catalog.models;

import com.example.security.objects.DBObject;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

// <-------------------------------- FROM CATALOG ----------------------------------> //

@Entity
@SQLDelete(sql="UPDATE Grade SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Grade extends DBObject {
    @Id
    @SequenceGenerator(
            name="grade_sequence",
            sequenceName = "grade_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "grade_sequence"
    )
    private int databaseGradeId;

    private UUID gradeId;
    private int value;

    //CascadeType.MERGE : copiaza obiectul intr-un obiect cu acelasi identificator
//    @ManyToOne(cascade = {CascadeType.MERGE})
    private String subject;

    private String evaluationDate;

    public Grade(){}


    public Grade(int value, String subject, String evaluationDate) {
        this.value = value;
        this.subject = subject;
        this.evaluationDate = evaluationDate;
    }

    public UUID getId() {
        return gradeId;
    }
    public void setId(UUID gradeId) {
        this.gradeId = gradeId;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                "value=" + value +
                ", subject=" + subject +
                ", evaluation date=" + evaluationDate +
                '}';
    }

    public Grade setDeleted(){
        super.setIsDeleted(true);
        return this;
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
