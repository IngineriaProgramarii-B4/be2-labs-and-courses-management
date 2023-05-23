package com.example.user.controller;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.services.StudentsService;
import com.example.subject.model.Subject;
import com.example.user.controllers.StudentsController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentsController.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogStudentsControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudentsService studentsService;
    private Student student;
    private Subject subject;
    private Grade grade;
    List<Student> students = new ArrayList<>();
    List<Grade> grades = new ArrayList<>();
    @BeforeEach
    void setUp(){
        subject = new Subject("Mocked", 6, 2, 3, null, null,null,false);
        student = new Student(
                UUID.randomUUID(),
                "Florin",
                "Rotaru",
                "florin.eugen@uaic.ro",
                "florin02",
                2,
                4,
                "123FAKE92929",
                new HashSet<>(Arrays.asList(new Subject())));
        grade = new Grade( 7, subject, new Date());

        student.addGrade(grade);
        students.add(student);
        grades.add(grade);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void whenGetAllStudents_thenReturnExistingStudents() throws Exception {
        // Arrange
        when(studentsService.getStudentsByParams(Map.of())).thenReturn(students);

        // Act
        MvcResult studentResult = mvc.perform(get("/api/v1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Student> studentResponse = objectMapper.readValue(studentResult.getResponse().getContentAsString(), new TypeReference<>() {});

        // Assert
        assertNotNull(studentResponse);
        assertEquals(students, studentResponse);
    }
    @Test
    void givenValidStudent_whenSaveStudent_thenReturnSameStudent() throws Exception {
        // Arrange
        when(studentsService.saveStudent(any(Student.class))).thenReturn(student);

        // Act
        MvcResult studentResult = mvc.perform(post("/api/v1/students")
                        .content(asJsonString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        Student studentCreated = objectMapper.readValue(studentResult.getResponse().getContentAsString(), Student.class);

        // Assert
        assertNotNull(studentCreated);
        assertEquals(student,studentCreated);

    }
    @Test
    void givenValidStudentId_whenGetStudentById_thenReturnsWantedStudent() throws Exception {
        // Arrange
        UUID id = student.getId();
        when(studentsService.getStudentById(id)).thenReturn(student);

        // Act
        // Test scenario where student is found
        MvcResult studentResult = mvc.perform(get("/api/v1/students/{id}", id.toString()))
                .andExpect(status().isOk())
                .andReturn();
        Student studentCreated = objectMapper.readValue(studentResult.getResponse().getContentAsString(), Student.class);

        // Assert
        assertNotNull(studentCreated);
        assertEquals(student, studentCreated);
    }

    @Test
    void givenInvalidStudentId_whenGetStudentById_thenReturnsStatusNotFound() throws Exception {
        // Test scenario where student is not found
        // Arrange
        UUID id = student.getId();
        when(studentsService.getStudentById(id)).thenReturn(null);
        // Act
        mvc.perform(get("/api/v1/students/{id}", id.toString()))
                // Assert
                .andExpect(status().isNotFound());
    }

    // givenValidStudentId_whenGetStudentGrades_thenReturnsStudentGrades()
    @Test
    void givenValidStudentId_whenGetStudentGrades_thenReturnsStudentGrades() throws Exception {
        // Mock the studentsService.getStudentById() method to return a non-null Student object
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);

        // Test the case where the Student object is present
        // Act
        MvcResult gradesList = mvc.perform(get("/api/v1/students/{id}/grades",  student.getId(), subject.getTitle())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Grade> gradesResponse = objectMapper.readValue(gradesList.getResponse().getContentAsString(), new TypeReference<>() {});

        // Assert
        assertNotNull(gradesResponse);

        long gradesIds = 0;
        long gradesResponseIds = 0;
        for (Grade grade1 : grades)
            gradesIds += grade1.getId().getLeastSignificantBits();
        for (Grade grade2 : gradesResponse)
            gradesResponseIds += grade2.getId().getLeastSignificantBits();

        assertEquals(gradesIds, gradesResponseIds);
    }
    @Test
    void givenNullStudent_whenGetStudentGrades_thenReturnsNull() throws Exception {
        // Mock the studentsService.getStudentById() method to return a null object
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(null);

        // Test the case where the Student object is not present
        // Act
        MvcResult nullStudentResult = mvc.perform(get("/api/v1/students/{id}/grades",  student.getId(), subject.getTitle())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Check that the response entity is null
        String nullStudentResponse = nullStudentResult.getResponse().getContentAsString();

        // Assert
        assertTrue(nullStudentResponse.isEmpty());
    }

    @Test
    void givenValidStudentAndGradeIds_whenGetStudentGradeById_thenReturnsWantedGrade() throws Exception {
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(grade);

        // Act
        MvcResult gradesList = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}",  student.getId().toString(),  grade.getId()))
                .andExpect(status().isOk())
                .andReturn();
        Grade gradeResponse = objectMapper.readValue(gradesList.getResponse().getContentAsString(),Grade.class);

        // Assert
        assertNotNull(gradeResponse);
        assertEquals(gradeResponse.getId(),grade.getId());
    }

    @Test
    void TestGetGradeByIdReturnsGradeWhenStudentAndGradeExist() throws Exception {
        // Arrange
        UUID studentId = student.getId();
        UUID gradeId = grade.getId();
        when(studentsService.getStudentById(studentId)).thenReturn(student);
        when(studentsService.getGradeById(studentId, gradeId)).thenReturn(grade);

        // Act
        MvcResult result = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}", studentId, gradeId))
                .andExpect(status().isOk())
                .andReturn();
        Grade gradeResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Grade.class);

        // Assert
        assertNotNull(gradeResponse);
        assertEquals(grade.getId(), gradeResponse.getId());
        assertTrue(result.getResponse().getContentAsString().contains(subject.getTitle()));
    }

    @Test
    void givenInvalidStudentId_whenGetStudentGradeById_thenReturnsNotFound() throws Exception {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID gradeId = grade.getId();
        when(studentsService.getStudentById(studentId)).thenReturn(null);

        // Act
        MvcResult result = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}", studentId, gradeId))
                .andExpect(status().isNotFound())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        // Assert
        assertEquals("", content);
    }

    @Test
    void givenInvalidGradeId_whenGetStudentGradeById_thenReturnsNotFound() throws Exception {
        // Arrange
        UUID studentId = student.getId();
        UUID gradeId = UUID.randomUUID();
        when(studentsService.getStudentById(studentId)).thenReturn(student);
        when(studentsService.getGradeById(studentId, gradeId)).thenReturn(null);

        // Act
        MvcResult result = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}", studentId, gradeId))
                .andExpect(status().isNotFound())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        // Assert
        assertEquals("", content);
    }

    @Test
    void givenInvalidStudentAndGradeIds_whenGetStudentGradeById_thenReturnsNotFound() throws Exception {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID gradeId = UUID.randomUUID();
        when(studentsService.getStudentById(studentId)).thenReturn(null);
        when(studentsService.getGradeById(studentId, gradeId)).thenReturn(null);

        // Act
        MvcResult result = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}", studentId, gradeId))
                .andExpect(status().isNotFound())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        // Assert
        assertEquals("", content);
    }

    @Test
    void givenGrade_whenSaveGrade_thenReturnsSameGrade() throws Exception {
        // Arrange
        Grade gradeForPost = new Grade(10, subject, new Date());
        gradeForPost.setId(UUID.randomUUID());
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.addGrade(eq(student.getId()), any(Grade.class))).thenAnswer((Answer<Grade>) invocationOnMock -> {
            student.addGrade(gradeForPost);
            return gradeForPost;
        });

        // Act
        // Posting grade for an existing student
        MvcResult postGradeResult = mvc.perform(post("/api/v1/students/{id}/grades", student.getId())
                        .content(asJsonString(gradeForPost))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Grade gradeResponse = objectMapper.readValue(postGradeResult.getResponse().getContentAsString(), Grade.class);

        // Assert
        assertNotNull(gradeResponse);
        assertEquals(gradeResponse.getSubject().getTitle(), gradeForPost.getSubject().getTitle());
        assertEquals(gradeResponse.getValue(), gradeForPost.getValue());
    }
    @Test
    void givenGradeAndInvalidStudentId_whenSaveGrade_thenReturnsNotFound() throws Exception {
        // Arrange
        // Non-existent student ID
        UUID nonExistentStudentId = UUID.randomUUID();
        when(studentsService.getStudentById(nonExistentStudentId)).thenReturn(null);
        Grade gradeForPost = new Grade(10, subject, new Date());
        gradeForPost.setId(UUID.randomUUID());

        // Posting grade for a non-existent student
        // Act
        MvcResult postGradeResultNotFound = mvc.perform(post("/api/v1/students/{id}/grades", nonExistentStudentId)
                        .content(asJsonString(gradeForPost))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void givenNullGrade_whenSaveGrade_thenReturnsNotFound() throws Exception {
        // Arrange
        Grade gradeForPost = new Grade(10, subject, new Date());
        gradeForPost.setId(UUID.randomUUID());
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.addGrade(eq(student.getId()), any(Grade.class))).thenAnswer((Answer<Grade>) invocationOnMock -> {
            student.addGrade(gradeForPost);
            return gradeForPost;
        });
        // Act
        mvc.perform(post("/api/v1/students/{id}/grades", student.getId())
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Assert
        assertEquals(1, student.getGrades().size());
    }
    @Test
    void givenGrade_whenDeleteGrade_thenReturnGradeDeleted() throws Exception {

        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteGrade(student.getId(), grade.getId())).thenAnswer((Answer<Grade>) invocationOnMock -> {
            grade.setDeleted();
            return grade;
        });

        // Act
        MvcResult deleteGradeResult = mvc.perform(delete("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .content(asJsonString(grade))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Grade gradeResponse = objectMapper.readValue(deleteGradeResult.getResponse().getContentAsString(), Grade.class);

        // Assert
        assertNotNull(gradeResponse);
        assertEquals(gradeResponse.getId(), grade.getId());
        assertTrue(grade.isDeleted());
    }

    @Test
    void givenGrade_whenDeleteGrade_thenReturnNotFound() throws Exception {

        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteGrade(student.getId(), grade.getId())).thenReturn(null);

        // Act
        MvcResult deleteGradeResult = mvc.perform(delete("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .content(asJsonString(grade))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        assertEquals(deleteGradeResult.getResponse().getContentAsString(), "");
    }

    @Test
    void givenInvalidStudentId_whenDeleteStudent_thenReturnsNotFound() throws Exception {
        // Arrange
        UUID nonExistentStudentId = UUID.randomUUID();
        when(studentsService.getStudentById(nonExistentStudentId)).thenReturn(null);

        // Act
        MvcResult deleteResult = mvc.perform(delete("/api/v1/students/{id}", nonExistentStudentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        assertEquals("", deleteResult.getResponse().getContentAsString());
    }


    @Test
    void givenValidStudentId_whenDeleteStudent_thenReturnsStudentDeleted() throws Exception {
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteStudent(student.getId())).thenAnswer((Answer<Student>) invocationOnMock -> (Student) student.setIsDeleted(true));

        // Act
        MvcResult deleteStudentResult = mvc.perform(delete("/api/v1/students/{id}", student.getId())
                        .content(asJsonString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Student studentDeleted = objectMapper.readValue(deleteStudentResult.getResponse().getContentAsString(), Student.class);

        // Assert
        assertNotNull(studentDeleted);
        assertTrue(studentDeleted.getIsDeleted());
    }
    @Test
    void givenValidStudentAndGrade_whenUpdateGrade_thenReturnUpdatedGrade() throws Exception {
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(grade);

        Date evalDateTest = new Date();
        Integer valueTest = 3;

        when(studentsService.updateGrade(eq(student.getId()), any(Integer.class), any(Date.class), eq(grade.getId()))).thenAnswer((Answer<Grade>) invocationOnMock -> {
            Grade toUpdate = student.getGradeById(grade.getId());
            toUpdate.setEvaluationDate(evalDateTest);
            toUpdate.setValue(valueTest);
            return toUpdate;
        });
        // Act
        // Test case 1: when student and grade are present
        MvcResult updateGradeResult = mvc.perform(put("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .param("value", asJsonString(valueTest))
                        .param("evaluationDateMs", asJsonString(evalDateTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Grade gradeResponse = objectMapper.readValue(updateGradeResult.getResponse().getContentAsString(), Grade.class);

        // Assert
        assertNotNull(gradeResponse);
    }

    @Test
    void givenInvalidStudent_whenUpdateGrade_thenReturnsNotFound() throws Exception {
        // Test case 2: when student is not present
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(null);
        Date evalDateTest = new Date();
        Integer valueTest = 3;
        // Act
        MvcResult updateGradeResult = mvc.perform(put("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .param("evaluationDateMs", asJsonString(evalDateTest))
                        .param("value", asJsonString(valueTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        assertEquals("", updateGradeResult.getResponse().getContentAsString());
    }
    @Test
    void givenInvalidGrade_whenUpdateGrade_thenReturnsNotFound() throws Exception {
        // Test case 3: when grade is not present
        // Arrange
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(null);
        Date evalDateTest = new Date();
        Integer valueTest = 3;
        // Act
        MvcResult updateGradeResult = mvc.perform(put("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .param("evaluationDateMs", asJsonString(evalDateTest))
                        .param("value", asJsonString(valueTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        assertEquals("", updateGradeResult.getResponse().getContentAsString());
    }
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}