package com.example.subject.dao;

import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Resource;
import com.example.subject.model.Subject;
import com.example.subject.repository.ComponentRepo;
import com.example.subject.repository.EvaluationRepo;
import com.example.subject.repository.ResourceRepo;
import com.example.subject.repository.SubjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectDataAccessServiceTest {

    @Mock
    private SubjectRepo subjectRepo;

    @Mock
    private Subject subject;

    @Mock
    private ComponentRepo componentRepo;

    @Mock
    private ResourceRepo resourceRepo;

    @Mock
    private EvaluationRepo evaluationRepo;

    @InjectMocks
    private SubjectDataAccessService subjectDas;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //SUBJECT

    @Test
    void insertSubject(){
        Subject testSubject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        int result = subjectDas.insertSubject(testSubject);
        assertEquals(1, result);
        verify(subjectRepo, times(1)).save(testSubject);
    }

    @Test
    void selectAllSubjects(){
        List<Subject> result = subjectDas.selectAllSubjects();
        assertEquals(0, result.size());
        verify(subjectRepo, times(1)).findAll();
    }

    @Test
    void selectSubjectByTitle(){
        Optional<Subject> result = subjectDas.selectSubjectByTitle("Test Subject");
        assertEquals(Optional.empty(), result);
        verify(subjectRepo, times(1)).findSubjectByTitle("Test Subject");
    }

    @Test
    void getSubjectsByYearAndSemester(){
        List<Subject> result = subjectDas.getSubjectsByYearAndSemester(1, 2);
        assertEquals(0, result.size());
        verify(subjectRepo, times(1)).findAllByYearAndSemester(1, 2);
    }

    @Test
    void deleteSubjectByTitleSubjectNotFound(){
        int result = subjectDas.deleteSubjectByTitle("Test Subject");
        assertEquals(0, result);
    }
    @Test
    void deleteSubjectByTitleOldImageNotNull(){
        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());

        Resource image = new Resource("image.jpg", "/path/to/image.jpg","type");
        subject.setImage(image);

        subjectRepo.save(subject);
        when(subjectRepo.findSubjectByTitle(subject.getTitle())).thenReturn(Optional.of(subject));

        int result = subjectDas.deleteSubjectByTitle(subject.getTitle());
        assertEquals(1, result);

        Resource oldImage = subject.getImage();
        assertNotNull(oldImage);
        assertTrue(oldImage.getIsDeleted());
        assertEquals("/path/to/DELETED_Test Subject_image.jpg", oldImage.getLocation());

        subject = subjectRepo.findSubjectByTitle(subject.getTitle()).orElse(null);
        assertNotNull(subject);
        assertTrue(subject.getIsDeleted());
        assertEquals(oldImage, subject.getImage());
    }

    @Test
    void deleteSubjectByTitleOldImageNull(){
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Test Subject")).thenReturn(Optional.of(subject));
        int result = subjectDas.deleteSubjectByTitle("Test Subject");
        assertEquals(1, result);
    }

    @Test
    void updateSubjectByTitleSubjectNotFound(){
        Mockito.when(subjectRepo.findSubjectByTitle("Math")).thenReturn(Optional.empty());

        // when
        Subject newSubject = new Subject();
        newSubject.setTitle("Mathematics");
        int result = subjectDas.updateSubjectByTitle("Math", newSubject);

        // then
        assertEquals(0, result);
        Mockito.verify(subjectRepo, Mockito.never()).save(Mockito.any(Subject.class));
    }

    @Test
    void updateSubjectByTitleWhereNewTitleDiffers(){
        // given
        Subject subject = new Subject();
        subject.setTitle("Math");
        subject.setCredits(4);
        subject.setYear(1);
        subject.setSemester(1);
        subject.setDescription("Math subject");
        List<Component> componentList = new ArrayList<>();
        Component component = new Component();
        component.setType("Course");
        List<Resource> resourceList = new ArrayList<>();
        Resource resource = new Resource();
        resource.setTitle("Lecture 1");
        resource.setLocation("/resources/Math/lectures/Lecture1.txt");
        resourceList.add(resource);
        component.setResources(resourceList);
        componentList.add(component);
        subject.setComponentList(componentList);
        Mockito.when(subjectRepo.findSubjectByTitle("Math")).thenReturn(Optional.of(subject));

        // when
        Subject newSubject = new Subject();
        newSubject.setTitle("Mathematics");
        newSubject.setCredits(5);
        newSubject.setYear(1);
        newSubject.setSemester(2);
        newSubject.setDescription("Mathematics subject");
        int result = subjectDas.updateSubjectByTitle("Math", newSubject);

        // then
        assertEquals(1, result);
        assertEquals("Mathematics", subject.getTitle());
        assertEquals(5, subject.getCredits());
        assertEquals(1, subject.getYear());
        assertEquals(2, subject.getSemester());
        assertEquals("Mathematics subject", subject.getDescription());
    }

    @Test
    void updateSubjectByTitleWhereNewTitleIsSame(){
        // given
        Subject subject = new Subject("Math", 4, 1, 1, "Math subject",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component("Course", 10, List.of(new Resource("Lecture 1", "/resources/Math/lectures/Lecture1.txt", "txt")));
        subject.setComponentList(List.of(component));
        Mockito.when(subjectRepo.findSubjectByTitle("Math")).thenReturn(Optional.of(subject));

        // when
        Subject newSubject = new Subject("Math", 5, 1, 2, "Mathematics subject",
                new ArrayList<>(), new ArrayList<>());
        int result = subjectDas.updateSubjectByTitle("Math", newSubject);

        // then
        assertEquals(1, result);
        assertEquals("Math", subject.getTitle());
        assertEquals(5, subject.getCredits());
        assertEquals(1, subject.getYear());
        assertEquals(2, subject.getSemester());
        assertEquals("Mathematics subject", subject.getDescription());
    }

    @Test
    void testUpdateSubjectWithOldImage() {
        Resource image = new Resource("image","location","type");
        Subject subject = new Subject("Title", 6, 1, 2, "Description",
                new ArrayList<>(), new ArrayList<>());
        subject.setImage(image);
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Title")).thenReturn(Optional.of(subject));

        Resource newImage = new Resource("newImage", "newImage.jpg", "jpg");
        subject.setImage(newImage);
        subjectDas.updateSubjectByTitle("Title", subject);

        Resource updatedImage = subject.getImage();
        assertNotNull(updatedImage);
    }

    @Test
    void saveImageToSubjectSubjectNotFound(){
        Resource image = new Resource("Image", "location", "type");
        int result = subjectDas.saveImageToSubject("Test Subject", image);
        assertEquals(0, result);
    }

    @Test
    void saveImageToSubjectSuccessful(){
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Test Subject")).thenReturn(Optional.of(subject));
        Resource image = new Resource("Image", "location", "type");
        int result = subjectDas.saveImageToSubject("Test Subject", image);
        assertEquals(1, result);
    }

    @Test
    void saveImageToSubject_WithOldImage() {
        Subject subject = new Subject("Math", 3, 2023, 2, "Mathematics subject", null, null);
        Resource oldImage = new Resource("Old Image", "location", "type");
        subject.setImage(oldImage);
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Math")).thenReturn(Optional.of(subject));

        Resource newImage = new Resource("Math_image_v2.jpg", "RESOURCE_PATH/Math_image_v2.jpg", "image");

        int result = subjectDas.saveImageToSubject("Math", newImage);

        assertEquals(1, result);
        Resource updatedImage = subject.getImage();
        assertNotNull(updatedImage);
        Resource savedImage = Objects.requireNonNull(subjectRepo.findSubjectByTitle("Math").orElse(null)).getImage();
        assertNotNull(savedImage);
        assertEquals("Math_image_v2.jpg", savedImage.getTitle());
    }

    //COMPONENT
    @Test
    void addComponentNoSubject(){
        Component component = new Component("Course", 10, new ArrayList<>());
        int result = subjectDas.addComponent("Test Subject", component);
        assertEquals(0, result);
    }

    @Test
    void addComponentSuccessful(){
        Component component = new Component("Course", 10, new ArrayList<>());
        subject.setComponentList(new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Test Subject")).thenReturn(Optional.of(subject));
        int result = subjectDas.addComponent("Test Subject", component);
        assertEquals(1, result);
    }

    @Test
    void getComponents(){
        List<Component> result = subjectDas.getComponents("Test Subject");
        assertEquals(0, result.size());
        verify(componentRepo, times(1)).findAllBySubjectTitle("Test Subject");
    }

    @Test
    void getComponentByType(){
        Optional<Component> result = subjectDas.getComponentByType("Test Subject", "Course");
        assertEquals(Optional.empty(), result);
        verify(componentRepo, times(1)).findBySubjectTitleAndType("Test Subject", "Course");
    }

    @Test
    void deleteComponentByTypeSubjectNotFound(){
        int result = subjectDas.deleteComponentByType("Test Subject", "Course");
        assertEquals(0, result);
    }
    @Test
    void deleteComponentByTypeComponentNotFound(){
        subject.setComponentList(new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle("Test Subject")).thenReturn(Optional.of(subject));
        int result = subjectDas.deleteComponentByType("Test Subject", "Course");
        assertEquals(0, result);
    }
    @Test
    void deleteComponentByTypeSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component( type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        int result = subjectDas.deleteComponentByType(title, type);

        assertEquals(1, result);

        Optional<Component> deletedComponent = componentRepo.findBySubjectTitleAndType(title, type);
        assertTrue(deletedComponent.isPresent());
        assertTrue(deletedComponent.get().getIsDeleted());
        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void updateComponentByTypeComponentNotFound(){
        int result = subjectDas.updateComponentByType("Test Subject", "Course", new Component());
        assertEquals(0, result);
    }

    @Test
    void updateComponentByTypeSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component(type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        int result = subjectDas.updateComponentByType(title, type, new Component());

        assertEquals(1, result);

        Optional<Component> updatedComponent = componentRepo.findBySubjectTitleAndType(title, type);
        assertTrue(updatedComponent.isPresent());
        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void getResourcesForComponentType(){
        List<Resource> result = subjectDas.getResourcesForComponentType("Test Subject", "Course");
        assertEquals(0, result.size());
        verify(resourceRepo, times(1)).findAllBySubjectTitleAndComponentType("Test Subject", "Course");
    }

    @Test
    void getResourceByTitleForComponentType(){
        Optional<Resource> result = subjectDas.getResourceByTitleForComponentType("Test Subject", "Course", "Test Resource");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void addResourceForComponentTypeComponentNotFound(){
        Resource resource = new Resource("Test Resource", "Test Link", "Test Description");
        int result = subjectDas.addResourceForComponentType("Test Subject", "Course", resource);
        assertEquals(0, result);
    }

    @Test
    void addResourceForComponentTypeSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component(type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        Resource resource = new Resource("Test Resource", "Test Link", "Test Description");
        int result = subjectDas.addResourceForComponentType(title, type, resource);

        assertEquals(1, result);

        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void updateResourceByTitleForComponentTypeResourceNotFound(){
        int result = subjectDas.updateResourceByTitleForComponentType("Test Subject", "Course", "Test Resource", new Resource());
        assertEquals(0, result);
    }

    @Test
    void updateResourceByTitleForComponentTypeSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component(type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        Resource resource = new Resource("Test Resource", "Test Link", "Test Description");
        subjectDas.addResourceForComponentType(title, type, resource);
        Mockito.when(resourceRepo.findBySubjectTitleAndComponentTypeAndResourceTitle(title, type, "Test Resource")).thenReturn(Optional.of(resource));

        int result = subjectDas.updateResourceByTitleForComponentType(title, type, "Test Resource", new Resource());

        assertEquals(1, result);

        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void deleteResourceByTitleForComponentTypeComponentNotFound(){
        int result = subjectDas.deleteResourceByTitleForComponentType("Test Subject", "Course", "Test Resource");
        assertEquals(0, result);
    }

    @Test
    void deleteResourceByTitleForComponentTypeResourceNotFound(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component(type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        int result = subjectDas.deleteResourceByTitleForComponentType(title, type, "Test Resource");

        assertEquals(0, result);
    }

    @Test
    void deleteResourceByTitleForComponentTypeSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        Component component = new Component(type, 10, new ArrayList<>());
        subject.addComponent(component);
        subjectRepo.save(subject);
        subjectDas.addComponent(title, component);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));
        Mockito.when(componentRepo.findBySubjectTitleAndType(title, type)).thenReturn(Optional.of(component));

        Resource resource = new Resource("Test Resource", "Test Link", "Test Description");
        subjectDas.addResourceForComponentType(title, type, resource);
        Mockito.when(resourceRepo.findBySubjectTitleAndComponentTypeAndResourceTitle(title, type, "Test Resource")).thenReturn(Optional.of(resource));

        int result = subjectDas.deleteResourceByTitleForComponentType(title, type, "Test Resource");

        assertEquals(1, result);

        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    //EVALUATION

    @Test
    void addEvaluationMethodSubjectNotFound(){
        Evaluation evaluation = new Evaluation("Course", 0.5F, "description");
        int result = subjectDas.addEvaluationMethod("Test Subject", evaluation);
        assertEquals(0, result);
    }

    @Test
    void addEvaluationSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));

        Evaluation evaluation = new Evaluation(type, 0.5F, "description");
        int result = subjectDas.addEvaluationMethod(title, evaluation);

        assertEquals(1, result);

        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void getEvaluationMethods(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));

        Evaluation evaluation = new Evaluation(type, 0.5F, "description");
        subjectDas.addEvaluationMethod(title, evaluation);
        Mockito.when(evaluationRepo.findAllBySubjectTitle(title)).thenReturn(new ArrayList<>(List.of(evaluation)));

        List<Evaluation> result = subjectDas.getEvaluationMethods(title);

        assertEquals(1, result.size());
    }

    @Test
    void getEvaluationMethodByComponent(){
        Optional<Evaluation> result = subjectDas.getEvaluationMethodByComponent("Test Subject", "Course");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void deleteEvaluationMethodSubjectNotFound(){
        int result = subjectDas.deleteEvaluationMethod("Test Subject", "Course");
        assertEquals(0, result);
    }

    @Test
    void deleteEvaluationMethodEvaluationNotFound(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));

        int result = subjectDas.deleteEvaluationMethod(title, type);

        assertEquals(0, result);
    }

    @Test
    void deleteEvaluationMethodSuccessful(){
        String title = "Test Subject";
        String type = "Course";

        Subject subject = new Subject("Test Subject", 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        subjectRepo.save(subject);
        Mockito.when(subjectRepo.findSubjectByTitle(title)).thenReturn(Optional.of(subject));

        Evaluation evaluation = new Evaluation(type, 0.5F, "description");
        subjectDas.addEvaluationMethod(title, evaluation);
        Mockito.when(evaluationRepo.findBySubjectTitleAndComponent(title, type)).thenReturn(Optional.of(evaluation));

        int result = subjectDas.deleteEvaluationMethod(title, type);

        assertEquals(1, result);

        Optional<Subject> updatedSubject = subjectRepo.findSubjectByTitle(title);
        assertTrue(updatedSubject.isPresent());
    }

    @Test
    void updateEvaluationMethodByComponentEvaluationNotFound(){
        int result = subjectDas.updateEvaluationMethodByComponent("Test Subject", "Course", new Evaluation());
        assertEquals(0, result);
    }

    @Test
    void updateEvaluationMethodByComponentSuccessful(){
        String title = "Test Subject";
        String type = "Course";
        Evaluation evaluation = new Evaluation(type, 0.5F, "description");

        Subject subject = new Subject(title, 6, 1, 2, "Test Description",
                new ArrayList<>(), new ArrayList<>());
        subjectRepo.save(subject);
        subjectDas.addEvaluationMethod(title, evaluation);
        Mockito.when(evaluationRepo.findBySubjectTitleAndComponent(title, type)).thenReturn(Optional.of(evaluation));

        Evaluation updatedEvaluation = new Evaluation(type, 0.5F, "updated description");
        int result = subjectDas.updateEvaluationMethodByComponent(title, type, updatedEvaluation);

        assertEquals(1, result);
    }
}




















