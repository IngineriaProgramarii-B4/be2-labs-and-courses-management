package com.example.subject.api;


import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Resource;
import com.example.subject.model.Subject;
import com.example.subject.service.SubjectService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    private MockMvc mockMvc;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    //passed
    @Test
    void getAllSubjects() {

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Math", 5, 1, 1, "description A", new ArrayList<>(), new ArrayList<>(), false));
        subjects.add(new Subject("English", 4, 1, 1, "description B", new ArrayList<>(), new ArrayList<>(), false));
        when(subjectService.getAllSubjects()).thenReturn(subjects);

        List<Subject> result = subjectController.getAllSubjects();

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

    //passed
    @Test
    void addSubject() throws Exception {
        when(subjectService.addSubject(any())).thenReturn(1);

        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "title": "Math",
                            "credits": 5,
                            "year": 1,
                            "semester": 1,
                            "description": "description",
                            "components": [],
                            "evaluations": [],
                            "isDeleted": false
                        }"""))
                .andExpect(status().isCreated());
    }

    //passed
    @Test
    void addSubjectThatAlreadyExists() {
        Subject subject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>(), false);
        when(subjectService.addSubject(any())).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.addSubject(subject));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        verify(subjectService, times(1)).addSubject(any());
    }

    //passed
    @Test
    void deleteSubjectByTitle(){
        String title = "Math";
        when(subjectService.deleteSubjectByTitle(title)).thenReturn(1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.deleteSubjectByTitle(title));
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        verify(subjectService, times(1)).deleteSubjectByTitle(title);
    }

    //passed
    @Test
    void deleteSubjectByTitleNotFound() {
        String title = "Math";
        when(subjectService.deleteSubjectByTitle(title)).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.deleteSubjectByTitle(title));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(subjectService, times(1)).deleteSubjectByTitle(title);
    }


    //passed
    @Test
    void updateSubjectById() {
        String title = "Math";
        Subject updatedSubject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>(), false);
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(subjectService.updateSubjectByTitle(title, updatedSubject)).thenReturn(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.updateSubjectByTitle(title, updatedSubject));

        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        assertEquals("Subject updated successfully", exception.getReason());
        verify(subjectService, times(1)).getSubjectByTitle(title);
        verify(subjectService, times(1)).updateSubjectByTitle(title, updatedSubject);
    }

    //passed
    @Test
    void updateSubjectByIdNotFound() {
        String title = "Physics";
        Subject updatedSubject = new Subject("Physics", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>(), false);
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.updateSubjectByTitle(title, updatedSubject));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(subjectService, times(1)).getSubjectByTitle(title);
        verify(subjectService, times(0)).updateSubjectByTitle(title, updatedSubject);
    }

    @Test
    void updateSubjectWithInvalidSubject(){
        String title = "Math";
        Subject updatedSubject = new Subject("Math", 5, 1, 2, "description", new ArrayList<>(), new ArrayList<>(), false);
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(subjectService.updateSubjectByTitle(title, updatedSubject)).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.updateSubjectByTitle(title, updatedSubject));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        verify(subjectService, times(1)).getSubjectByTitle(title);
        verify(subjectService, times(1)).updateSubjectByTitle(title, updatedSubject);
    }

    //passed
    @Test
    void getSubjectByTitle() {
        Subject subject = new Subject("Algebraic Foundations of Science", 5, 1, 2, "not gonna pass", new ArrayList<>(), new ArrayList<>(), false);
        subjectService.addSubject(subject);

        when(subjectService.getSubjectByTitle("Algebraic Foundations of Science")).thenReturn(Optional.of(subject));

        Subject result = subjectController.getSubjectByTitle("Algebraic Foundations of Science");

        assertEquals("Algebraic Foundations of Science", result.getTitle());
        assertEquals(5, result.getCredits());
        assertEquals(1, result.getYear());
        assertEquals(2, result.getSemester());
        assertEquals("not gonna pass", result.getDescription());
        assertEquals(0, result.getComponentList().size());
        assertEquals(0, result.getEvaluationList().size());
    }

    //passed
    @Test
    void getSubjectByTitleNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.getSubjectByTitle("Math"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    //passed
    @Test
    void getSubjectsByYearAndSemesterSuccessful() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Physics", 5, 1, 2, "description", List.of(new Component("Course", 10, List.of(new Resource("Book.pdf", "savedResources/Book.pdf", "application/pdf", false)), false)),
                List.of(new Evaluation("Course", 0.5f, "description B", false)), false));
        when(subjectService.getSubjectsByYearAndSemester(1, 2)).thenReturn(subjects);

        List<Subject> result = subjectController.getSubjectsByYearAndSemester(1, 2);

        assertEquals(1, result.size());
        assertEquals("Physics", result.get(0).getTitle());
        assertEquals(5, result.get(0).getCredits());
        assertEquals(1, result.get(0).getYear());
        assertEquals(2, result.get(0).getSemester());
        assertEquals("description", result.get(0).getDescription());
        assertEquals(1, result.get(0).getComponentList().size());
        assertEquals(1, result.get(0).getEvaluationList().size());


        verify(subjectService, times(1)).getSubjectsByYearAndSemester(1, 2);
    }

    MultipartFile file = mock(MultipartFile.class);
    @Test
    void uploadSubjectImageSuccessful(){
        String title = "Math";
        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(subjectService.uploadSubjectImage(title, file)).thenReturn(1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.uploadSubjectImage(title, file));
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        assertEquals("Image uploaded successfully", exception.getReason());
    }

    @Test
    void testUploadSubjectImageNotFound(){
        String title = "Test Title";

        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.uploadSubjectImage(title, file));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Subject not found", exception.getReason());
    }

    @Test
    void testUploadSubjectImageInvalid(){
        String title = "Test Title";

        when(subjectService.getSubjectByTitle(title)).thenReturn(Optional.of(new Subject()));
        when(subjectService.uploadSubjectImage(title, file)).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> subjectController.uploadSubjectImage(title, file));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Image is invalid", exception.getReason());
    }

    @Test
    void testGetSubjectImageNotFound() {
        when(subjectService.getSubjectByTitle(anyString())).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = subjectController.getSubjectImage("Test");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Subject not found", new String(Objects.requireNonNull(response.getBody())));
    }

    @Test
    void testGetSubjectImageNoImage(){
        String title = "Test Subject";

        Subject subject = new Subject();
        subject.setTitle(title);
        subject.setImage(null);
        Optional<Subject> subjectMaybe = Optional.of(subject);

        // Mock the behavior of the subjectService
        when(subjectService.getSubjectByTitle(title)).thenReturn(subjectMaybe);

        ResponseEntity<byte[]> response = subjectController.getSubjectImage(title);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Image not found", new String(Objects.requireNonNull(response.getBody())));
    }


    @Test
    void testGetSubjectImageSuccess() throws Exception {
        Subject mockSubject = new Subject();
        Resource resource = new Resource("IP_romania.png", "savedResources\\IP_romania.png", "image/png", false);
        mockSubject.setImage(resource);

        when(subjectService.getSubjectByTitle("test")).thenReturn(Optional.of(mockSubject));

        String absolutePath = new File("").getAbsolutePath();
        String path = mockSubject.getImage().getLocation();
        String filePath = absolutePath + "\\" + path;
        System.out.println(filePath);

        byte[] mockImageBytes = Files.readAllBytes(new File(filePath).toPath());

        ResponseEntity<byte[]> response = subjectController.getSubjectImage("test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(mockImageBytes, response.getBody());
    }

}