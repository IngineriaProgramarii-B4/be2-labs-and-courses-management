package com.example.subject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {
    Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject();
    }

    @Test
    void setTitle() {
        subject.setTitle("Maths");
        assertEquals("Maths", subject.getTitle());
    }

    @Test
    void setCredits() {
        subject.setCredits(5);
        assertEquals(5, subject.getCredits());
    }

    @Test
    void setYear() {
        subject.setYear(1);
        assertEquals(1, subject.getYear());
    }

    @Test
    void setSemester() {
        subject.setSemester(2);
        assertEquals(2, subject.getSemester());
    }

    @Test
    void setDescription() {
        subject.setDescription("description");
        assertEquals("description", subject.getDescription());
    }

    @Test
    void setComponentList() {
        Component component = new Component("Course", 14, new ArrayList<>());
        subject.setComponentList(List.of(component));
        assertEquals(component, subject.getComponentList().get(0));
    }

    @Test
    void setEvaluationList() {
        Evaluation evaluation = new Evaluation("Course", 0.5f, "evaluation description");
        subject.setEvaluationList(List.of(evaluation));
        assertEquals(evaluation, subject.getEvaluationList().get(0));
    }

    @Test
    void setDeleted() {
        subject.setDeleted(true);
        assertTrue(subject.getIsDeleted());
    }

    @Test
    void setImage() {
        Resource image = new Resource("Resource", "RESOURCE_PATH/Maths_Resource", "image/jpeg");
        subject.setImage(image);
        assertEquals("Resource", subject.getImage().getTitle());
        assertEquals("RESOURCE_PATH/Maths_Resource", subject.getImage().getLocation());
        assertEquals("image/jpeg", subject.getImage().getType());
    }

    @Test
    void addComponent() {
        Component component = new Component("Course", 14, new ArrayList<>());
        subject.addComponent(component);
        assertEquals(1, subject.getComponentList().size());
        assertEquals(component, subject.getComponentList().get(0));
    }

    @Test
    void removeComponent() {
        Component component = new Component("Course", 14, new ArrayList<>());
        subject.addComponent(component);
        assertEquals(1, subject.getComponentList().size());

        subject.removeComponent(component);
        assertEquals(0, subject.getComponentList().size());
    }

    @Test
    void softDeleteComponent() {
        Component component = new Component("Course", 14, new ArrayList<>());
        subject.addComponent(component);
        assertEquals(1, subject.getComponentList().size());

        subject.softDeleteComponent(component);
        assertEquals(1, subject.getComponentList().size());
        assertTrue(subject.getComponentList().get(0).getIsDeleted());
    }

    @Test
    void softDeleteComponentNotFound() {
        Component component = new Component("Course", 14, new ArrayList<>());
        assertEquals(0, subject.getComponentList().size());

        subject.softDeleteComponent(component);
        assertEquals(0, subject.getComponentList().size());
    }

    @Test
    void addEvaluation() {
        Evaluation evaluation = new Evaluation("Course", 0.5f, "evaluation description");
        subject.addEvaluation(evaluation);
        assertEquals(1, subject.getEvaluationList().size());
        assertEquals(evaluation, subject.getEvaluationList().get(0));
    }

    @Test
    void removeEvaluation() {
        Evaluation evaluation = new Evaluation("Course", 0.5f, "evaluation description");
        subject.addEvaluation(evaluation);
        assertEquals(1, subject.getEvaluationList().size());

        subject.removeEvaluation(evaluation);
        assertEquals(0, subject.getEvaluationList().size());
    }

    @Test
    void softDeleteEvaluation() {
        Evaluation evaluation = new Evaluation("Course", 0.5f, "evaluation description");
        subject.addEvaluation(evaluation);
        assertEquals(1, subject.getEvaluationList().size());

        subject.softDeleteEvaluation(evaluation);
        assertEquals(1, subject.getEvaluationList().size());
        assertTrue(subject.getEvaluationList().get(0).getIsDeleted());
    }

    @Test
    void softDeleteEvaluationNotFound() {
        Evaluation evaluation = new Evaluation("Course", 0.5f, "evaluation description");
        assertEquals(0, subject.getEvaluationList().size());

        subject.softDeleteEvaluation(evaluation);
        assertEquals(0, subject.getEvaluationList().size());
    }

    @Test
    void testToString() {
        Subject subject = new Subject();
        subject.setTitle("Maths");
        subject.setCredits(5);
        subject.setYear(1);
        subject.setSemester(2);
        subject.setDescription("description");

        String expected = "Subject{" +
                "title='Maths', " +
                "credits=5, " +
                "year=1, " +
                "semester=2, " +
                "description='description', " +
                "componentList=" + subject.getComponentList() + ", " +
                "evaluationList=" + subject.getEvaluationList() + ", " +
                "image=" + subject.getImage() +
                "}";
        assertEquals(expected, subject.toString());
    }
}