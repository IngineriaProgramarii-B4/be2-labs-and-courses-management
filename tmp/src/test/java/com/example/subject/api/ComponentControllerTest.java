package com.example.subject.api;


import com.example.subject.model.Component;
import com.example.subject.model.Subject;
import com.example.subject.service.ComponentService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComponentControllerTest {

    @Mock
    private SubjectService subjectService;
    @Mock
    private ComponentService componentService;

    @InjectMocks
    private ComponentController componentController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getComponents() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        List<Component> components = new ArrayList<>();
        components.add(new Component( "Seminar", 14, new ArrayList<>(), false));
        when(componentService.getComponents(subject.getTitle())).thenReturn(components);

        List<Component> result = componentController.getComponents(subject.getTitle());

        assertEquals(1, result.size());
        assertEquals("Seminar", result.get(0).getType());
        assertEquals(14, result.get(0).getNumberWeeks());
        assertEquals(0, result.get(0).getResources().size());
    }

    @Test
    void testAddComponentSuccessful() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        Component component = new Component("Seminar", 14, new ArrayList<>(), false);

        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.addComponent(title, component)).thenReturn(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.addComponent(title, component));
        assertEquals(HttpStatus.CREATED, exception.getStatusCode());
        verify(componentService, times(1)).addComponent(title, component);
    }

    @Test
    void testAddInvalidComponent() {
        Subject subject = new Subject("Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component("Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        Component component = new Component("Seminar", 14, new ArrayList<>(), false);

        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.addComponent(title, component)).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.addComponent(title, component));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        verify(componentService, times(1)).addComponent(title, component);
    }

    @Test
    void testAddComponentInvalidSubject() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component("Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        Component component = new Component("Seminar", 14, new ArrayList<>(), false);

        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.addComponent(title, component));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(0)).addComponent(title, component);
    }

    @Test
    void getComponentByTypeSuccessful() {
        Subject subject = new Subject("Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Component component = new Component("Seminar", 14, new ArrayList<>(), false);
        when(componentService.getComponentByType(title, "Seminar")).thenReturn(Optional.of(component));

        Component result = componentController.getComponentByType(title, "Seminar");

        assertEquals("Seminar", result.getType());
        assertEquals(14, result.getNumberWeeks());
        assertEquals(0, result.getResources().size());
    }

    @Test
    void getComponentByTypeInvalidSubject() {
        Subject subject = new Subject("Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component("Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.getComponentByType(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(0)).getComponentByType(title, "Seminar");
    }

    @Test
    void getComponentByTypeInvalidComponent() {
        Subject subject = new Subject("Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component("Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(title, "Seminar")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.getComponentByType(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(1)).getComponentByType(title, "Seminar");
    }

    @Test
    void deleteComponentByTypeSuccessful() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.deleteComponentByType(title, "Seminar")).thenReturn(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.deleteComponentByType(title, "Seminar"));
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        verify(componentService, times(1)).deleteComponentByType(title, "Seminar");
    }

    @Test
    void deleteComponentByTypeInvalidSubject() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.deleteComponentByType(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(0)).deleteComponentByType(title, "Seminar");
    }

    @Test
    void deleteComponentByTypeInvalidComponent() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(componentService.deleteComponentByType(title, "Seminar")).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.deleteComponentByType(title, "Seminar"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(1)).deleteComponentByType(title, "Seminar");
    }

    @Test
    void updateComponentByType() {
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        String type = "Seminar";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        Component component = new Component("Seminar", 10, new ArrayList<>(), false);
        when(componentService.getComponentByType(title, type)).thenReturn(Optional.of(component));

        when(componentService.updateComponentByType(title, type, component)).thenReturn(1);

        componentController.updateComponentByType(title, "Seminar", component);
        verify(subjectService, times(1)).getSubjectByTitle(title);
        verify(componentService, times(1)).updateComponentByType(title, "Seminar", component);
    }

    @Test
    void updateComponentByTypeInvalidSubject() {
        Component testComponent = new Component( "Seminar", 10, new ArrayList<>(), false);
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        subjectService.addSubject(subject);
        String title = "Algebraic Foundations of Science";
        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.updateComponentByType(title, "Seminar", testComponent));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(componentService, times(0)).updateComponentByType(title, "Seminar", new Component());
    }

    @Test
    void updateComponentByTypeInitialComponentInvalid(){
        Component testComponent = new Component("Seminar", 10, new ArrayList<>(), false);
        Subject subject = new Subject( "Algebraic Foundations of Science", 6, 1, 2, "not gonna pass",
                List.of(new Component( "Seminar", 14, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        String title = "Algebraic Foundations of Science";
        String type = "Seminar";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(title, type)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.updateComponentByType(title, type, testComponent), "Expected a NOT_FOUND ResponseStatusException for component");
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Component not found", exception.getReason());
    }

    @Test
    void updateComponentByTypeBadRequest() {
        Component testComponent = new Component( "Seminar", 14, new ArrayList<>(), false);
        String title = "Maths";
        String type = "Seminar";
        Subject subject = new Subject("Maths", 4, 1, 1, """
                    This course is designed to provide students with a comprehensive understanding of mathematical concepts and principles. The course covers a wide range of topics, including algebra, calculus, statistics, geometry, and trigonometry.
                    Students will learn how to solve complex mathematical problems, develop analytical and critical thinking skills, and enhance their ability to reason logically. They will also develop a deep appreciation for the beauty and elegance of mathematics and its practical applications in various fields, such as engineering, physics, finance, and computer science.
                    The course includes lectures, interactive discussions, and hands-on activities to help students grasp abstract mathematical concepts and apply them to real-world problems. Students will also have opportunities to collaborate with their peers, engage in group projects, and receive individualized feedback from the instructor.
                    Upon completion of the course, students will have a solid foundation in mathematics, which will prepare them for advanced courses in math or related disciplines, as well as careers in fields that require strong quantitative skills.""",
                List.of(new Component("Course", 10, new ArrayList<>(), false)),
                new ArrayList<>(), false);
        Component component = new Component("Course", 10, new ArrayList<>(), false);

        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(subject));
        when(componentService.getComponentByType(title, type)).thenReturn(Optional.of(component));
        when(componentService.updateComponentByType(title, type, testComponent)).thenReturn(0);

        assertThrows(ResponseStatusException.class, () -> componentController.updateComponentByType(title, type, testComponent), "Expected a BAD_REQUEST ResponseStatusException");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> componentController.updateComponentByType(title, type, testComponent), "Expected a BAD_REQUEST ResponseStatusException");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}