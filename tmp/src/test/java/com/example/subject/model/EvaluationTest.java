package com.example.subject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {
    Evaluation evaluation;

    @BeforeEach
    void setUp() {
        evaluation = new Evaluation();
    }

    @Test
    void setComponent() {
        evaluation.setComponent("Course");
        assertEquals("Course", evaluation.getComponent());
    }

    @Test
    void setValue() {
        evaluation.setValue(0.5f);
        assertEquals(0.5f, evaluation.getValue());
    }

    @Test
    void setDescription() {
        evaluation.setDescription("evaluation description");
        assertEquals("evaluation description", evaluation.getDescription());
    }

    @Test
    void setDeleted() {
        evaluation.setDeleted(true);
        assertTrue(evaluation.isDeleted());
    }

    @Test
    void testToString() {
        evaluation.setComponent("Course");
        evaluation.setValue(0.5f);

        String expected = "Evaluation{" +
                "component='Course', " +
                "value=0.5" +
                "}";
        assertEquals(expected, evaluation.toString());
    }
}
