package com.example.subject.api;

import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Subject;
import com.example.subject.service.ComponentService;
import com.example.subject.service.EvaluationService;
import com.example.subject.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationControllerTest {

    @Mock
    private SubjectService subjectService;

    @Mock
    private EvaluationService evaluationService;

    @Mock
    private ComponentService componentService;

    @InjectMocks
    private EvaluationController evaluationController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getEvaluationMethods() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false),
                        new Evaluation("Laboratory", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        List<Evaluation> evaluations = List.of(new Evaluation( "Seminar", 0.5F, "Test", false),
                        new Evaluation("Laboratory", 0.5F, "Exam", false));
        when(evaluationService.getEvaluationMethods(subject.getTitle())).thenReturn(evaluations);

        List<Evaluation> result = evaluationController.getEvaluationMethods(subject.getTitle());
        assertEquals(evaluations, result);
        assertEquals(2, result.size());
        assertEquals("Seminar", result.get(0).getComponent());
        assertEquals("Laboratory", result.get(1).getComponent());
        assertEquals(0.5F, result.get(0).getValue());
        assertEquals(0.5F, result.get(1).getValue());
        assertEquals("Test", result.get(0).getDescription());
        assertEquals("Exam", result.get(1).getDescription());
    }

    @Test
    void getEvaluationMethodsSubjectNotFound(){
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.getEvaluationMethods(title));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getEvaluationMethodByComponent() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false),
                        new Evaluation("Laboratory", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar"))
                .thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));
        Evaluation evaluation = new Evaluation( "Seminar", 0.5F, "Test", false);
        when(evaluationService.getEvaluationMethodByComponent(subject.getTitle(), "Seminar")).thenReturn(Optional.of(evaluation));
        Evaluation result = evaluationController.getEvaluationMethodByComponent(subject.getTitle(), "Seminar");
        assertEquals(evaluation.getId(), result.getId());
        assertEquals(evaluation.getComponent(), result.getComponent());
        assertEquals(evaluation.getValue(), result.getValue());
        assertEquals(evaluation.getDescription(), result.getDescription());
    }

    @Test
    void getEvaluationMethodByComponentSubjectNotFound(){
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.getEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Subject not found", exception.getReason());
    }

    @Test
    void getEvaluationMethodByComponentComponentNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false),
                        new Evaluation("Laboratory", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.empty());

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.getEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Component not found", exception.getReason());
    }

    @Test
    void getEvaluationMethodByComponentEvaluationNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false),
                        new Evaluation("Laboratory", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar"))
                .thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));
        when(evaluationService.getEvaluationMethodByComponent(subject.getTitle(), "Seminar")).thenReturn(Optional.empty());

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.getEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Evaluation method not found", exception.getReason());
    }

    @Test
    void addEvaluationSuccessful(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Evaluation evaluation = new Evaluation("Seminar", 0.5F, "Exam", false);

        when(componentService.getComponentByType(subject.getTitle(), evaluation.getComponent()))
                .thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));

        when(evaluationService.addEvaluationMethod(subject.getTitle(), evaluation)).thenReturn(1);

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.CREATED, exception.getStatusCode());
    }

    @Test
    void addEvaluationReturnsNotAcceptableForInvalidEval(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                        , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Evaluation evaluation = new Evaluation("Course", 1.5F, "Exam", true);
        when(componentService.getComponentByType(subject.getTitle(), evaluation.getComponent())).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
    }
    /*@Test
    void addEvaluationReturnsNotAcceptableForDeletedEval(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Evaluation evaluation = new Evaluation("Course", 0.3F, "Exam", true);
        when(componentService.getComponentByType(subject.getTitle(), evaluation.getComponent())).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
    }
    @Test
    void addEvaluationReturnsNotAcceptableForEvaluationInvalidValue(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Evaluation evaluation = new Evaluation("Course", 1.2F, "Exam", true);
        when(componentService.getComponentByType(subject.getTitle(), evaluation.getComponent())).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
    }*/
    @Test
    void addEvaluationSubjectNotFound(){
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());
        Evaluation evaluation = new Evaluation("Laboratory", 0.5F, "Exam", false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void addEvaluationComponentNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Evaluation evaluation = new Evaluation("Laboratory", 0.5F, "Exam", false);
        when(componentService.getComponentByType(subject.getTitle(), evaluation.getComponent())).thenReturn(Optional.empty());

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.addEvaluationMethod(title, evaluation));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deleteEvaluationMethodByComponent(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));
        when(evaluationService.deleteEvaluationMethodByComponent(subject.getTitle(), "Seminar")).thenReturn(1);

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.deleteEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        assertEquals("Evaluation method deleted successfully", exception.getReason());
    }

    @Test
    void deleteEvaluationMethodByComponentSubjectNotFound(){
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.deleteEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Subject not found", exception.getReason());
    }

    @Test
    void deleteEvaluationMethodByComponentComponentNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.empty());

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.deleteEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Component not found", exception.getReason());
    }

    @Test
    void deleteEvaluationMethodByComponentEvaluationNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.deleteEvaluationMethodByComponent(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Evaluation method not found", exception.getReason());
    }

    @Test
    void updateEvaluationMethodByComponent(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));
        Evaluation evaluation = new Evaluation( "Seminar", 0.5F, "Exam", false);
        when(evaluationService.updateEvaluationMethodByComponent(subject.getTitle(), "Seminar", evaluation)).thenReturn(1);

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.updateEvaluationMethodByComponent(title, "Seminar", evaluation));
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        assertEquals("Evaluation method updated successfully", exception.getReason());
    }

    @Test
    void updateEvaluationMethodByComponentSubjectNotFound(){
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());
        Evaluation evaluation = new Evaluation( "Seminar", 0.5F, "Exam", false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.updateEvaluationMethodByComponent(title, "Seminar", evaluation));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Subject not found", exception.getReason());
    }

    @Test
    void updateEvaluationMethodByComponentComponentNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                List.of(new Evaluation( "Seminar", 0.5F, "Test", false))
                , false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.empty());
        Evaluation evaluation = new Evaluation( "Seminar", 0.5F, "Exam", false);

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.updateEvaluationMethodByComponent(title, "Seminar", evaluation));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Component not found", exception.getReason());
    }

    @Test
    void updateEvaluationMethodByComponentEvaluationNotFound(){
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false),
                        new Component("Laboratory", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(subject.getTitle(), "Seminar")).thenReturn(Optional.of(new Component( "Seminar", 14, new ArrayList<>(), false)));
        Evaluation evaluation = new Evaluation( "Seminar", 0.5F, "Exam", false);

        String title = "Algebraic Foundations of Science";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> evaluationController.updateEvaluationMethodByComponent(title, "Seminar", evaluation));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Evaluation method not found", exception.getReason());
    }

}