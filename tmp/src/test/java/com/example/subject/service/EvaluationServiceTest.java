package com.example.subject.service;


import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private CourseDao courseDao;

    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        evaluationService = new EvaluationService(courseDao);
    }

    @Test
    void ValidateEvalReturnsTrue() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false),
                        new Component("Seminar", 14, new ArrayList<>(),false)),
                List.of(new Evaluation("Course", 0.3f, "Exam", false)), false);
        String title = subject.getTitle();
        Evaluation eval = new Evaluation("Seminar", 0.2f, "Exam", false);

        when(courseDao.getEvaluationMethods(title)).thenReturn(List.of(eval));
        when(courseDao.getComponents(title)).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false),
                new Component("Seminar", 14, new ArrayList<>(),false)));

        // Test the method
        boolean result = evaluationService.validateEval(title, eval);

        // Verify the result
        assertTrue(result);
    }

    @Test
    void validateEvalReturnsFalseForInvalidComponent() {
        Evaluation evaluation = new Evaluation("Invalid", 0.2f, "Exam", false);
        assertFalse(evaluationService.validateEval("Maths", evaluation));
    }

    @Test
    void validateEvalReturnsFalseForInvalidValue(){
        Evaluation evaluation = new Evaluation("Course", 1.2f, "Exam", false);
        assertFalse(evaluationService.validateEval("Maths", evaluation));
    }

    @Test
    void validateEvalReturnsFalseForDeletedEvaluation(){
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", true);
        assertFalse(evaluationService.validateEval("Maths", evaluation));
    }

    @Test
    void validateComponentReturnsFalseForInvalidComponent() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false),
                        new Component("Seminar", 14, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        when(courseDao.getComponents(subject.getTitle())).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false),
                new Component("Seminar", 14, new ArrayList<>(),false)));
        assertFalse(evaluationService.validateComponent("Maths", "Laboratory"));
        assertFalse(evaluationService.validateComponent("Maths", "Invalid"));
    }

    @Test
    void validateComponentReturnsTrueForValidComponent() {
        List<Component> components = List.of(new Component("Seminar", 10, new ArrayList<>(),false),
                new Component("Course", 14, new ArrayList<>(),false));

        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                components, new ArrayList<>(), false);
        when(courseDao.getComponents(subject.getTitle())).thenReturn(components);
        assertTrue(evaluationService.validateComponent("Maths", "Course"));
    }

    @Test
    void getEvaluationMethods() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getEvaluationMethods("Maths")).thenReturn(List.of(evaluation));
        assertEquals(List.of(evaluation), evaluationService.getEvaluationMethods("Maths"));
    }

    @Test
    void getEvaluationMethodByComponentSuccessful(){
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getComponents("Maths")).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false)));
        when(courseDao.getEvaluationMethodByComponent("Maths", "Course")).thenReturn(Optional.of(evaluation));
        assertEquals(Optional.of(evaluation), evaluationService.getEvaluationMethodByComponent("Maths", "Course"));
    }

    @Test
    void getEvaluationMethodByComponentReturnsEmptyOptionalForInvalidComponent() {
        assertEquals(Optional.empty(), evaluationService.getEvaluationMethodByComponent("Maths", "Invalid"));
    }
    @Test
    void addEvaluationMethodSuccessful() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        assertEquals(0, subject.getEvaluationList().size());
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getComponents("Maths")).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false)));
        when(courseDao.addEvaluationMethod("Maths", evaluation)).thenReturn(1);
        assertEquals(1, evaluationService.addEvaluationMethod("Maths", evaluation));
    }

    @Test
    void addEvaluationMethodReturnsFalseForExistentComponentEval(){
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getComponents("Maths")).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false)));
        when(courseDao.getEvaluationMethodByComponent("Maths", "Course")).thenReturn(Optional.of(evaluation));

        Evaluation eval = new Evaluation("Course", 0.2f, "Exam", false);
        assertEquals(0, evaluationService.addEvaluationMethod("Maths", eval));
    }

    @Test
    void addEvaluationMethodReturnsFalseForInvalidComponent() {
        Evaluation evaluation = new Evaluation("Invalid", 0.2f, "Exam", false);
        assertEquals(0, evaluationService.addEvaluationMethod("Maths", evaluation));
    }

    @Test
    void deleteEvaluationMethodByComponentSuccessful() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getComponents("Maths")).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false)));
        when(courseDao.deleteEvaluationMethod("Maths", "Course")).thenReturn(1);
        assertEquals(1, evaluationService.deleteEvaluationMethodByComponent("Maths", "Course"));
    }

    @Test
    void deleteEvaluationMethodByComponentReturnsFalseForInvalidComponent() {
        assertEquals(0, evaluationService.deleteEvaluationMethodByComponent("Maths", "Invalid"));
    }

    @Test
    void updateEvaluationMethodByComponentSuccessful() {
        Subject subject = new Subject("Maths", 4, 1, 1, "description",
                List.of(new Component("Course", 10, new ArrayList<>(),false)),
                new ArrayList<>(), false);
        Evaluation evaluation = new Evaluation("Course", 0.2f, "Exam", false);
        subject.setEvaluationList(List.of(evaluation));
        when(courseDao.getComponents("Maths")).thenReturn(List.of(new Component("Course", 10, new ArrayList<>(),false)));
        when(courseDao.updateEvaluationMethodByComponent("Maths", "Course", evaluation)).thenReturn(1);
        assertEquals(1, evaluationService.updateEvaluationMethodByComponent("Maths", "Course", evaluation));
    }

    @Test
    void updateEvaluationMethodByComponentReturnsFalseForInvalidComponent() {
        assertEquals(0, evaluationService.updateEvaluationMethodByComponent("Maths", "Invalid", new Evaluation()));
    }
}