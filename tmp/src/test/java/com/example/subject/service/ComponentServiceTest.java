package com.example.subject.service;

import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Resource;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComponentServiceTest {

    @Mock
    private CourseDao courseDao;

    private ComponentService componentService;

    @BeforeEach
    void setUp() {
        componentService = new ComponentService(courseDao);
    }

    @Test
    void validateComponentReturnsTrueForValidComponent() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(new Subject()));
        assertTrue(componentService.validateComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void validateComponentReturnsFalseForDeletedComponent(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        component.setDeleted(true);
        assertFalse(componentService.validateComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void validateComponentReturnsFalseForInvalidType(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        component.setType("Invalid");
        assertFalse(componentService.validateComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void validateComponentReturnsFalseForInvalidNumberWeeks(){
        Component component = new Component("Seminar", 0, new ArrayList<>());
        assertFalse(componentService.validateComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void validateComponentReturnsFalseForSubjectNotFound(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());
        assertFalse(componentService.validateComponent("Algebraic Foundations of Science", component));
    }
    @Test
    void validateComponentToUpdateReturnsTrueForValidComponent() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass",
                List.of(new Component("Course", 14, new ArrayList<>())), new ArrayList<>());
        Component component = new Component("Seminar", 14, new ArrayList<>());
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        assertNotEquals(component.getType(), subject.getComponentList().get(0).getType());
        assertTrue(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void testValidateComponent_DuplicateComponent() {
        List<Component> components = List.of(
                new Component("Course", 12, new ArrayList<>()),
                new Component("Laboratory", 13, new ArrayList<>()));
        Subject subject = new Subject();
        subject.setTitle("title");
        subject.setComponentList(components);
        when(courseDao.selectSubjectByTitle("title")).thenReturn(Optional.of(subject));
        when(courseDao.getComponents("title")).thenReturn(components);
        Component component = new Component("Course", 14, new ArrayList<>());
        boolean result = componentService.validateComponent("title", component);
        assertFalse(result);
    }

    @Test
    void validateComponentToUpdateReturnsFalseForDeletedComponent(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        component.setDeleted(true);
        assertFalse(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void validateComponentToUpdateReturnsFalseForInvalidNumberWeeks(){
        Component component = new Component("Seminar", 0, new ArrayList<>());
        assertFalse(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void validateComponentToUpdateReturnsFalseForInvalidType(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        component.setType("Invalid");
        assertFalse(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void validateComponentToUpdateReturnsFalseForSubjectNotFound(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());
        assertFalse(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void validateComponentToUpdateReturnsFalseForDifferentType(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        assertFalse(componentService.validateComponentToUpdate("Algebraic Foundations of Science", "Laboratory", component));
    }
    @Test
    void validateTypeReturnsTrueForValidType() {
        assertTrue(componentService.validateType("Seminar"));
        assertTrue(componentService.validateType("Laboratory"));
        assertTrue(componentService.validateType("Course"));
    }

    @Test
    void validateTypeReturnsFalseForInvalidType() {
        assertFalse(componentService.validateType("type"));
    }

    @Test
    void addComponentReturnsFalseForInvalidComponent() {
        Component component = new Component("Seminar", 0, new ArrayList<>());
        assertEquals(0, componentService.addComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void addComponentSuccessful(){
        Component component = new Component("Seminar", 14, new ArrayList<>());
        Subject subject = new Subject();
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(courseDao.addComponent("Algebraic Foundations of Science", component)).thenReturn(1);
        assertEquals(1, componentService.addComponent("Algebraic Foundations of Science", component));
    }

    @Test
    void getComponents() {
        List<Component> components = List.of(new Component("Seminar", 14, new ArrayList<>()),
                new Component("Laboratory", 10, new ArrayList<>()));
        Subject subject = new Subject();
        subject.setComponentList(components);
        when(courseDao.getComponents("Algebraic Foundations of Science")).thenReturn(components);
        List<Component> result = componentService.getComponents("Algebraic Foundations of Science");
        assertEquals(components, result);
    }

    @Test
    void getComponentByTypeSuccessful() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        when(courseDao.getComponentByType("Algebraic Foundations of Science", "Seminar")).thenReturn(Optional.of(component));
        Optional<Component> result = componentService.getComponentByType("Algebraic Foundations of Science", "Seminar");
        assertEquals(Optional.of(component), result);
    }

    @Test
    void getComponentByTypeReturnsEmptyOptionalForInvalidType() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        Optional<Component> result = componentService.getComponentByType("Algebraic Foundations of Science", "Invalid");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void deleteComponentByTypeSuccessfulNoResources() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(courseDao.deleteComponentByType("Algebraic Foundations of Science", "Seminar")).thenReturn(1);
        assertEquals(1, componentService.deleteComponentByType("Algebraic Foundations of Science", "Seminar"));
    }


    @Test
    void deleteComponentByTypeDeletesResources() {
        Subject subject = new Subject();
        subject.setTitle("Mathematics");
        when(courseDao.selectSubjectByTitle("Mathematics")).thenReturn(Optional.of(subject));
        when(courseDao.getResourcesForComponentType("Mathematics", "Course")).thenReturn(List.of( new Resource("Book", "location", "Course")));
        assertNotEquals(Optional.empty(), courseDao.selectSubjectByTitle("Mathematics"));
        assertNotEquals(null, courseDao.getComponents("Mathematics"));
        assertNotEquals(null, courseDao.getResourcesForComponentType("Mathematics", "Course"));
        //when(courseDao.deleteResourceByTitleForComponentType("Mathematics", "Course", "Book")).thenReturn(1);
        when(courseDao.deleteComponentByType("Mathematics", "Course")).thenReturn(1);
        int result = componentService.deleteComponentByType("Mathematics", "Course");

        assertEquals(1, result);
    }


    @Test
    void deleteComponentByTypeEvaluationPresent() {
        when(courseDao.selectSubjectByTitle("Mathematics")).thenReturn(Optional.of(new Subject()));
        when(courseDao.getResourcesForComponentType("Mathematics", "Course")).thenReturn(new ArrayList<>());
        when(courseDao.getEvaluationMethodByComponent("Mathematics", "Course"))
                .thenReturn(Optional.of(new Evaluation("Course", 0.5F, "Exam")));

        when(courseDao.deleteEvaluationMethod("Mathematics", "Course")).thenReturn(1);
        when(courseDao.deleteComponentByType("Mathematics", "Course")).thenReturn(1);
        int result = componentService.deleteComponentByType("Mathematics", "Course");

        assertEquals(1, result);
    }


    @Test
    void deleteComponentByTypeReturnsZeroForInvalidComponentType() {
        Component component = new Component("Invalid", 0, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        subject.setTitle("Algebraic Foundations of Science");
        assertFalse(componentService.validateType(component.getType()));
        assertEquals(0, componentService.deleteComponentByType("Algebraic Foundations of Science", "Invalid"));
    }

    @Test
    void deleteComponentByTypeReturnsZeroForInvalidSubject() {
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.empty());
        assertEquals(0, componentService.deleteComponentByType("Algebraic Foundations of Science", "Seminar"));
    }

    @Test
    void updateComponentByTypeSuccessful() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(courseDao.updateComponentByType("Algebraic Foundations of Science", "Seminar", component)).thenReturn(1);
        assertEquals(1, componentService.updateComponentByType("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void updateComponentByTypeReturnsZeroForInvalidSubject() {
        Component component = new Component("Seminar", 14, new ArrayList<>());
        assertEquals(0, componentService.updateComponentByType("Algebraic Foundations of Science", "Seminar", component));
    }

    @Test
    void updateComponentByTypeReturnsZeroForInvalidComponent() {
        Component component = new Component("Invalid", 0, new ArrayList<>());
        Subject subject = new Subject();
        subject.setComponentList(List.of(component));
        assertEquals(0, componentService.updateComponentByType("Algebraic Foundations of Science", "Seminar", component));
    }
}