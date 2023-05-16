package com.example.catalog.models;

import com.example.security.objects.DBObject;
import com.example.subject.model.Subject;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.UUID;

// <-------------------------------- FROM CATALOG ----------------------------------> //

@Entity
@SQLDelete(sql="UPDATE Grade SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Subject subject;

    private Date evaluationDate;

    private boolean deleted=false;
    public Grade(){}


    public Grade(int value, Subject subject, Date evaluationDate) {
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
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

    public boolean isDeleted(){
        return deleted;
    }

    public Grade setDeleted(){
        deleted=true;
        return this;
    }
}
