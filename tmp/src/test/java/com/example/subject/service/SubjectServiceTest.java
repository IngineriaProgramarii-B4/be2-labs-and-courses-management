package com.example.subject.service;

import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Resource;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {
    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void validateUpdateTestSameTitles() {
        SubjectService subjectService = new SubjectService(courseDao);

        boolean result = subjectService.validateUpdate("Maths", "Maths");
        assertTrue(result);
    }

    @Test
    void validateUpdateTestDuplicateTitle() {
        SubjectService subjectService = new SubjectService(courseDao);
        Subject subject = new Subject("Physics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));

        boolean result = subjectService.validateUpdate("Maths", "Physics");
        assertFalse(result);
    }

    @Test
    void validateUpdateTestSuccessful() {
        SubjectService subjectService = new SubjectService(courseDao);
        Subject subject = new Subject("Physics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));

        boolean result = subjectService.validateUpdate("Maths", "History");
        assertTrue(result);
    }

    @Test
    void validateTitleTestTitleFound() {
        Subject subject1 = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        Subject subject2 = new Subject("Physics", 4, 2, 3, "description", new ArrayList<>(), new ArrayList<>());

        courseDao.insertSubject(subject1);
        courseDao.insertSubject(subject2);
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject1, subject2));

        assertTrue(subjectService.validateTitle("Maths"));
    }

    @Test
    void validateTitleTestTitleNotFound() {
        Subject subject1 = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        Subject subject2 = new Subject("Physics", 4, 2, 3, "description", new ArrayList<>(), new ArrayList<>());

        courseDao.insertSubject(subject1);
        courseDao.insertSubject(subject2);

        assertFalse(subjectService.validateTitle("Introduction to Cryptography"));
    }

    @Test
    void validateComponentsTestDuplicateComponentType() {
        List<Component> components = new ArrayList<>();
        Component component1 = new Component("Course", 4, new ArrayList<>());
        Component component2 = new Component("Course", 5, new ArrayList<>());
        components.add(component1);
        components.add(component2);
        Subject subject = new Subject("Maths", 5, 1, 2, "description", components, new ArrayList<>());

        assertFalse(subjectService.validateComponents(subject));
    }

    @Test
    void validateComponentsTestSuccessful() {
        List<Component> components = new ArrayList<>();
        Component component1 = new Component("Course", 4, new ArrayList<>());
        Component component2 = new Component("Seminar", 5, new ArrayList<>());
        components.add(component1);
        components.add(component2);
        Subject subject = new Subject("Maths", 5, 1, 2, "description", components, new ArrayList<>());

        assertTrue(subjectService.validateComponents(subject));
    }

    @Test
    void validateComponentsTestSuccessfulNoComponents() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        assertTrue(subjectService.validateComponents(subject));
    }

    @Test
    void validateSubjectTestTitleEmpty() {
        Subject subject = new Subject("", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        boolean result = subjectService.validateSubject(subject);
        assertFalse(result);
    }

    @Test
    void validateSubjectTestIsDeleted() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        subject.setDeleted(true);
        boolean result = subjectService.validateSubject(subject);
        assertFalse(result);
    }

    @Test
    void validateSubjectTestDuplicateTitle() {
        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        subjects.add(subject);
        when(courseDao.selectAllSubjects()).thenReturn(subjects);

        Subject subject2 = new Subject("Maths", 1, 3, 1, "description", new ArrayList<>(), new ArrayList<>());

        boolean result = subjectService.validateSubject(subject2);
        assertFalse(result);
    }

    @Test
    void validateSubjectTestSuccessfulNoComponents() {
        List<Subject> subjects = new ArrayList<>();
        Subject subject1 = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        subjects.add(subject1);
        when(courseDao.selectAllSubjects()).thenReturn(subjects);

        Subject subject2 = new Subject("Physics", 1, 3, 1, "description", new ArrayList<>(), new ArrayList<>());

        boolean result = subjectService.validateSubject(subject2);
        assertTrue(result);
    }

    @Test
    void validateSubjectTestDuplicateComponentTypes() {
        Component component1 = new Component("Course", 2, new ArrayList<>());
        Component component2 = new Component("Course", 2, new ArrayList<>());
        Component component3 = new Component("Seminar", 3, new ArrayList<>());
        List<Component> components = List.of(component1, component2, component3);

        Subject subject = new Subject("Maths", 5, 1, 2, "description", components, new ArrayList<>());
        boolean result = subjectService.validateSubject(subject);
        assertFalse(result);
    }

    @Test
    void validateSubjectTestSuccessful() {
        Component component1 = new Component("Course", 2, new ArrayList<>());
        Component component2 = new Component("Laboratory", 2, new ArrayList<>());
        Component component3 = new Component("Seminar", 3, new ArrayList<>());
        List<Component> components = List.of(component1, component2, component3);

        Subject subject = new Subject("Maths", 5, 1, 2, "description", components, new ArrayList<>());
        boolean result = subjectService.validateSubject(subject);
        assertTrue(result);
    }

    @Test
    void validateSubjectTestDeleted() {
        Component component1 = new Component("Course", 2, new ArrayList<>());
        Component component2 = new Component("Course", 2, new ArrayList<>());
        Component component3 = new Component("Seminar", 3, new ArrayList<>());
        List<Component> components = List.of(component1, component2, component3);

        Subject subject = new Subject("Maths", 5, 1, 2, "description", components, new ArrayList<>());
        boolean result = subjectService.validateSubject(subject);
        assertFalse(result);
    }

    @Test
    void getAllSubjectsTest() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Math", 5, 1, 1, "description A", new ArrayList<>(), new ArrayList<>()));
        subjects.add(new Subject("English", 4, 1, 1, "description B", new ArrayList<>(), new ArrayList<>()));
        when(courseDao.selectAllSubjects()).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getTitle());
        assertEquals("English", result.get(1).getTitle());
        assertEquals(5, result.get(0).getCredits());
        assertEquals(4, result.get(1).getCredits());
        assertEquals(1, result.get(0).getYear());
        assertEquals(1, result.get(1).getYear());
        assertEquals(1, result.get(0).getSemester());
        assertEquals(1, result.get(1).getSemester());
        assertEquals("description A", result.get(0).getDescription());
        assertEquals("description B", result.get(1).getDescription());
        assertEquals(0, result.get(0).getComponentList().size());
        assertEquals(0, result.get(1).getComponentList().size());
        assertEquals(0, result.get(0).getEvaluationList().size());
        assertEquals(0, result.get(1).getEvaluationList().size());
    }

    @Test
    void getSubjectByTitleTestExists() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);

        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));

        Optional<Subject> resultSuccess = subjectService.getSubjectByTitle("Algebraic Foundations of Science");

        assertTrue(resultSuccess.isPresent());
        assertEquals("Algebraic Foundations of Science", resultSuccess.get().getTitle());
        assertEquals(5, resultSuccess.get().getCredits());
        assertEquals(1, resultSuccess.get().getYear());
        assertEquals(2, resultSuccess.get().getSemester());
        assertEquals("not gonna pass", resultSuccess.get().getDescription());
        assertEquals(0, resultSuccess.get().getComponentList().size());
        assertEquals(0, resultSuccess.get().getEvaluationList().size());
    }

    @Test
    void getSubjectByTitleTestEmpty() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);

        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));

        Optional<Subject> resultEmpty = subjectService.getSubjectByTitle("Non-existent Subject");
        assertFalse(resultEmpty.isPresent());
    }

    @Test
    void getSubjectsByYearAndSemesterTest() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Physics", 5, 1, 2, "description", List.of(new Component("Course", 10, List.of(new Resource("Book.pdf", "savedResources/Book.pdf", "application/pdf")))),
                List.of(new Evaluation("Course", 0.5f, "description B"))));
        when(courseDao.getSubjectsByYearAndSemester(1, 2)).thenReturn(subjects);

        List<Subject> result = subjectService.getSubjectsByYearAndSemester(1, 2);

        assertEquals(1, result.size());
        assertEquals("Physics", result.get(0).getTitle());
        assertEquals(5, result.get(0).getCredits());
        assertEquals(1, result.get(0).getYear());
        assertEquals(2, result.get(0).getSemester());
        assertEquals("description", result.get(0).getDescription());
        assertEquals(1, result.get(0).getComponentList().size());
        assertEquals(1, result.get(0).getEvaluationList().size());

        verify(courseDao, times(1)).getSubjectsByYearAndSemester(1, 2);
    }

    @Test
    void addSubjectTestSuccessful() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        when(courseDao.insertSubject(subject)).thenReturn(1);

        assertEquals(1, subjectService.addSubject(subject));
    }

    @Test
    void addSubjectTestValidationFailure() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        //isDeleted == true => validation failure

        assertEquals(0, subjectService.addSubject(subject));
    }

    @Test
    void addSubjectThatAlreadyExistsTest() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);
        assertEquals(0, subjectService.addSubject(subject));
    }

    @Test
    void addSubjectWithSameNameTest() {
        Subject subject1 = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        Subject subject2 = new Subject("Algebraic Foundations of Science", 2, 3, 2, "yes gonna pass", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject1);

        assertEquals(0, subjectService.addSubject(subject1));
        assertEquals(0, subjectService.addSubject(subject2));
    }

    @Test
    void deleteSubjectByTitleTestSuccessful() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);

        when(courseDao.selectSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.deleteSubjectByTitle("Algebraic Foundations of Science")).thenReturn(1);

        assertEquals(1, subjectService.deleteSubjectByTitle("Algebraic Foundations of Science"));
    }

    @Test
    void deleteSubjectByTitleTestImageRenamed() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File imageFile = new File(folderPath + "Maths_image.jpg");
        File newFile = new File(folderPath + "DELETED_Maths_image.jpg");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        Resource image = new Resource("image.jpg", folderPath + "Maths_image.jpg", "image/jpeg");
        subject.setImage(image);
        courseDao.insertSubject(subject);

        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.deleteSubjectByTitle("Maths")).thenReturn(1);

        int result = subjectService.deleteSubjectByTitle("Maths");
        assertFalse(imageFile.exists());
        assertTrue(newFile.exists());
        assertEquals(1, result);

        if (imageFile.exists())
            imageFile.delete();
        newFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }

    @Test
    void deleteSubjectByTitleTestResourcesRenamed() {
        Component component = new Component("Course", 14, new ArrayList<>());
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File resourceFile = new File(folderPath + "Maths_Course_image.jpg");
        File updatedResourceFile = new File(folderPath + "DELETED_Maths_Course_image.jpg");
        try {
            resourceFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        component.addResource(new Resource(
                "image.jpg",
                resourceFile.getParentFile().getAbsolutePath() + "/Maths_Course_image.jpg",
                "image/jpeg"
        ));
        subject.addComponent(component);
        courseDao.insertSubject(subject);

        when(courseDao.getComponents("Maths")).thenReturn(List.of(component));
        when(courseDao.getResourcesForComponentType("Maths", "Course")).thenReturn(component.getResources());
        when(courseDao.getResourceByTitleForComponentType("Maths", "Course", "image.jpg")).thenReturn(Optional.of(component.getResources().get(0)));
        when(courseDao.deleteResourceByTitleForComponentType("Maths", "Course", "image.jpg")).thenReturn(1);
        when(courseDao.getEvaluationMethodByComponent("Maths", "Course")).thenReturn(Optional.empty());
        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.deleteSubjectByTitle("Maths")).thenReturn(1);

        int result = subjectService.deleteSubjectByTitle("Maths");
        assertFalse(resourceFile.exists());
        assertTrue(updatedResourceFile.exists());
        assertEquals(1, result);

        if (resourceFile.exists())
            resourceFile.delete();
        updatedResourceFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }

    @Test
    void deleteSubjectByTitleTestValidationFailure() {
        assertEquals(0, subjectService.deleteSubjectByTitle("Non-existent subject"));
    }

    @Test
    void deleteSubjectByTitleTestNotFound() {
        Subject subject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());

        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.selectSubjectByTitle("Math")).thenReturn(Optional.empty()); //impossible but SonarQube demands it
        when(courseDao.deleteSubjectByTitle("Math")).thenReturn(1);

        assertEquals(1, subjectService.deleteSubjectByTitle("Math"));
    }

    @Test
    void updateSubjectByTitleTestSuccessful() {
        String title = "Math";
        Subject updatedSubject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        when(courseDao.selectSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(courseDao.updateSubjectByTitle(title, updatedSubject)).thenReturn(1);

        assertEquals(1, subjectService.updateSubjectByTitle(title, updatedSubject));
        verify(courseDao, times(1)).selectSubjectByTitle(title);
        verify(courseDao, times(1)).updateSubjectByTitle(title, updatedSubject);
    }

    @Test
    void updateSubjectByTitleTestValidationFailure() {
        Subject subject1 = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        Subject subject2 = new Subject("Physics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        Subject subject3 = new Subject("Introduction to Cryptography", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject1);
        courseDao.insertSubject(subject2);

        assertEquals(0, subjectService.updateSubjectByTitle("Math", subject3));
    }

    @Test
    void updateSubjectByTitleTestInvalidSubject() {
        String title = "Math";
        Subject updatedSubject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        when(courseDao.selectSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(courseDao.updateSubjectByTitle(title, updatedSubject)).thenReturn(0);

        assertEquals(0, subjectService.updateSubjectByTitle(title, updatedSubject));
        verify(courseDao, times(1)).selectSubjectByTitle(title);
        verify(courseDao, times(1)).updateSubjectByTitle(title, updatedSubject);
    }

    @Test
    void updateSubjectByTitleTestNotFound() {
        String title = "Math";
        Subject updatedSubject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        when(courseDao.selectSubjectByTitle(title)).thenReturn(Optional.empty());

        int result = subjectService.updateSubjectByTitle("Math", updatedSubject);
        assertEquals(0, result);
    }

    @Test
    void updateSubjectByTitleTestImageRenamed() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File imageFile = new File(folderPath + "Maths_image.jpg");
        File newFile = new File(folderPath + "Mathematics_image.jpg");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        subject.setImage(new Resource(
                "image.jpg",
                imageFile.getParentFile().getAbsolutePath() + "/Maths_image.jpg",
                "image/jpeg"
        ));
        courseDao.insertSubject(subject);
        Subject updatedSubject = new Subject("Mathematics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());

        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.updateSubjectByTitle("Maths", updatedSubject)).thenReturn(1);

        int result = subjectService.updateSubjectByTitle("Maths", updatedSubject);
        assertFalse(imageFile.exists());
        assertTrue(newFile.exists());
        assertEquals(1, result);

        if (imageFile.exists())
            imageFile.delete();
        newFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }

    @Test
    void updateSubjectByTitleTestResourcesRenamed() {
        Component component = new Component("Course", 14, new ArrayList<>());
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File resourceFile = new File(folderPath + "Maths_Course_image.jpg");
        File updatedResourceFile = new File(folderPath + "Mathematics_Course_image.jpg");
        try {
            resourceFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        component.addResource(new Resource(
                "image.jpg",
                resourceFile.getParentFile().getAbsolutePath() + "/Maths_Course_image.jpg",
                "image/jpeg"
        ));
        subject.addComponent(component);
        courseDao.insertSubject(subject);
        Subject updatedSubject = new Subject("Mathematics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());

        when(courseDao.getComponents("Maths")).thenReturn(List.of(component));
        when(courseDao.getResourcesForComponentType("Maths", "Course")).thenReturn(component.getResources());
        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.updateSubjectByTitle("Maths", updatedSubject)).thenReturn(1);

        int result = subjectService.updateSubjectByTitle("Maths", updatedSubject);
        assertFalse(resourceFile.exists());
        assertTrue(updatedResourceFile.exists());
        assertEquals(1, result);

        if (resourceFile.exists())
            resourceFile.delete();
        updatedResourceFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }

    @Test
    void uploadSubjectImageTestDAOFailure() {
        MultipartFile image = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertEquals(0, subjectService.uploadSubjectImage("Math", image));
    }

    @Test
    void uploadSubjectImageTestSubjectNotFound() {
        String title = "Non-Existent Subject";
        assertEquals(0, subjectService.uploadSubjectImage(title, null));
    }

    @Test
    void uploadSubjectImageTestUploadIsSuccessful() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        courseDao.insertSubject(subject);
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File imageFile = new File(folderPath + "Maths_image.jpg");
        MultipartFile image = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                new byte[0]
        );

        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.saveImageToSubject(any(String.class), any(Resource.class))).thenReturn(1);

        assertEquals(1, subjectService.uploadSubjectImage("Maths", image));
        assertTrue(imageFile.exists());

        imageFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }

    @Test
    void uploadSubjectImageTestOldImageRenamed() {
        Subject subject = new Subject("Maths", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>());
        String absolutePath = new File("").getAbsolutePath();
        String folderPath = absolutePath + "/" + "savedResources/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File oldImageFile = new File(folderPath + "Maths_image.jpg");
        File newImageFile = new File(folderPath + "Maths_newImage.jpg");
        try {
            oldImageFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        subject.setImage(new Resource(
                        "image.jpg",
                        oldImageFile.getParentFile().getAbsolutePath() + "/Maths_image.jpg",
                        "image/jpeg"
                )
        );
        oldImageFile = new File(folderPath + "OUTDATED_Maths_image.jpg");
        courseDao.insertSubject(subject);

        MultipartFile image = new MockMultipartFile(
                "newImage.jpg",
                "newImage.jpg",
                "image/jpeg",
                new byte[0]
        );

        when(courseDao.selectSubjectByTitle("Maths")).thenReturn(Optional.of(subject));
        when(courseDao.selectAllSubjects()).thenReturn(List.of(subject));
        when(courseDao.saveImageToSubject(any(String.class), any(Resource.class))).thenReturn(1);

        assertEquals(1, subjectService.uploadSubjectImage("Maths", image));
        assertTrue(oldImageFile.exists());
        assertTrue(newImageFile.exists());

        oldImageFile.delete();
        newImageFile.delete();
        File[] filesInSavedResources = folder.listFiles();
        if (filesInSavedResources == null || filesInSavedResources.length == 0)
            folder.delete();
    }
}