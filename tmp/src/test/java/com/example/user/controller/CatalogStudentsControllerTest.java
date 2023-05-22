package com.example.user.controller;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.services.StudentsService;
import com.example.subject.model.Subject;
import com.example.subject.service.SubjectService;
import com.example.user.controllers.StudentsController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    @MockBean
    private SubjectService subjectService;
    private Student student;
    private String subject;
    private Grade grade;
    List<Student> students = new ArrayList<>();
    List<Grade> grades = new ArrayList<>();
    @BeforeEach
    void setUp(){
        subject = "IP";
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
        grade = new Grade( 7, subject, "12.12.2012");

        student.addGrade(grade);
        students.add(student);
        grades.add(grade);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void getAllStudentsTest() throws Exception {
        when(studentsService.getStudentsByParams(Map.of())).thenReturn(students);
        MvcResult studentResult = mvc.perform(get("/api/v1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Student> studentResponse = objectMapper.readValue(studentResult.getResponse().getContentAsString(), new TypeReference<>() {});

        assertNotNull(studentResponse);
        assertEquals(students, studentResponse);
    }
    @Test
    void createStudentTest() throws Exception {
        when(studentsService.saveStudent(any(Student.class))).thenReturn(student);
        MvcResult studentResult = mvc.perform(post("/api/v1/students")
                        .content(asJsonString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Student studentCreated = objectMapper.readValue(studentResult.getResponse().getContentAsString(), Student.class);
        assertNotNull(studentCreated);
        assertEquals(student,studentCreated);

    }
    @Test
    void getStudentByIdTest() throws Exception {
        UUID id = student.getId();
        when(studentsService.getStudentById(id)).thenReturn(student);

        // Test scenario where student is found
        MvcResult studentResult = mvc.perform(get("/api/v1/students/{id}", id.toString()))
                .andExpect(status().isOk())
                .andReturn();
        Student studentCreated = objectMapper.readValue(studentResult.getResponse().getContentAsString(), Student.class);
        assertNotNull(studentCreated);
        assertEquals(student, studentCreated);

        // Test scenario where student is not found
        when(studentsService.getStudentById(id)).thenReturn(null);
        mvc.perform(get("/api/v1/students/{id}", id.toString()))
                .andExpect(status().isNotFound());
    }


    /* In StudentsController la ce ati mutat voi nu exista acest get, am vazut ca e doar in Catalog  - am mutat si getu asta */

    @Test
    void getStudentByIdGradesTest() throws Exception {
        // Mock the studentsService.getStudentById() method to return a non-null Student object
        when(studentsService.getStudentById(student.getId())).thenReturn(student);

        // Test the case where the Student object is present
        MvcResult gradesList = mvc.perform(get("/api/v1/students/{id}/grades",  student.getId(), subject)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Grade> gradesResponse = objectMapper.readValue(gradesList.getResponse().getContentAsString(), new TypeReference<>() {});

        assertNotNull(gradesResponse);

        long gradesIds = 0;
        long gradesResponseIds = 0;
        for (Grade grade1 : grades)
            gradesIds += grade1.getId().getLeastSignificantBits();
        for (Grade grade2 : gradesResponse)
            gradesResponseIds += grade2.getId().getLeastSignificantBits();

        assertEquals(gradesIds, gradesResponseIds);

        // Mock the studentsService.getStudentById() method to return a null object
        when(studentsService.getStudentById(student.getId())).thenReturn(null);

        // Test the case where the Student object is not present
        MvcResult nullStudentResult = mvc.perform(get("/api/v1/students/{id}/grades",  student.getId(), subject)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // Check that the response entity is null
        String nullStudentResponse = nullStudentResult.getResponse().getContentAsString();
        assertTrue(nullStudentResponse.isEmpty());
    }


    /* Nici acest GET nu exista, exista doar put / delete cu acest endpoint  - solved */
    @Test
    void getStudentByIdGetGradeByIdAPI() throws Exception {
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(grade);

        MvcResult gradesList = mvc.perform(get("/api/v1/students/{id}/grades/{gradeId}",  student.getId().toString(),  grade.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Grade gradeResponse = objectMapper.readValue(gradesList.getResponse().getContentAsString(),Grade.class);

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
        assertTrue(result.getResponse().getContentAsString().contains(subject));
    }

    @Test
    void TestGetGradeByIdReturnsNotFoundWhenStudentDoesNotExist() throws Exception {
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
    void getGradeByIdReturnsNotFoundWhenGradeDoesNotExist() throws Exception {
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
    void TestGetGradeByIdReturnsNotFoundWhenStudentAndGradeDoNotExist() throws Exception {
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

//    @Test
//    void createGradeTest() throws Exception {
//        //GradeId pus in postGradeResult inainte ca metoda Student.addGrade() sa aloce un ID.
//        Grade gradeForPost = new Grade(10, subject, "12.12.2012");
//        gradeForPost.setId(UUID.randomUUID());
//
//        // Non-existent student ID
//        UUID nonExistentStudentId = UUID.randomUUID();
//        when(studentsService.getStudentById(nonExistentStudentId)).thenReturn(null);
//
//        when(studentsService.getStudentById(student.getId())).thenReturn(student);
//        when(studentsService.addGrade(eq(student.getId()), any(Grade.class))).thenAnswer((Answer<Grade>) invocationOnMock -> {
//            student.addGrade(gradeForPost);
//            return gradeForPost;
//        });
//
//        // Posting grade for a non-existent student
//        MvcResult postGradeResultNotFound = mvc.perform(post("/api/v1/students/{id}/grades", nonExistentStudentId)
//                        .content(asJsonString(gradeForPost))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        // Posting grade for an existing student
//        MvcResult postGradeResult = mvc.perform(post("/api/v1/students/{id}/grades", student.getId())
//                        .content(asJsonString(gradeForPost))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        Grade gradeResponse = objectMapper.readValue(postGradeResult.getResponse().getContentAsString(), Grade.class);
//
//        assertNotNull(gradeResponse);
//        assertEquals(gradeResponse.getSubject(), gradeForPost.getSubject());
//        assertEquals(gradeResponse.getValue(), gradeForPost.getValue());
//
//        // given null grade
//        mvc.perform(post("/api/v1/students/{id}/grades", student.getId())
//                        .content(asJsonString(null))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                // then
//                .andExpect(status().isBadRequest());
//
//        assertEquals(2, student.getGrades().size());
//    }



    @Test
    void deleteGradeTest() throws Exception {

        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteGrade(student.getId(), grade.getId())).thenAnswer((Answer<Grade>) invocationOnMock -> {
           return grade.setDeleted();
        });

        MvcResult deleteGradeResult = mvc.perform(delete("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .content(asJsonString(grade))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Grade gradeResponse = objectMapper.readValue(deleteGradeResult.getResponse().getContentAsString(), Grade.class);

        assertNotNull(gradeResponse);
        assertEquals(gradeResponse.getId(), grade.getId());
        assertTrue(gradeResponse.getIsDeleted());
    }

    @Test
    void deleteGradeAltTest() throws Exception {

        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteGrade(student.getId(), grade.getId())).thenReturn(null);

        MvcResult deleteGradeResult = mvc.perform(delete("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .content(asJsonString(grade))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals( "",deleteGradeResult.getResponse().getContentAsString());
    }

    @Test
    void deleteNonExistentStudentTest() throws Exception {
        UUID nonExistentStudentId = UUID.randomUUID();

        when(studentsService.getStudentById(nonExistentStudentId)).thenReturn(null);

        MvcResult deleteResult = mvc.perform(delete("/api/v1/students/{id}", nonExistentStudentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("", deleteResult.getResponse().getContentAsString());
    }


    @Test
    void deleteStudentTest() throws Exception {
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.deleteStudent(student.getId())).thenAnswer((Answer<Student>) invocationOnMock -> (Student) student.setIsDeleted(true));

        MvcResult deleteStudentResult = mvc.perform(delete("/api/v1/students/{id}", student.getId())
                        .content(asJsonString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Student studentDeleted = objectMapper.readValue(deleteStudentResult.getResponse().getContentAsString(), Student.class);

        assertNotNull(studentDeleted);
        assertTrue(studentDeleted.getIsDeleted());
    }

    /* Aici am vorbit cu tudor ca va trebui schimbata modul de a afisa data si vedem dupa aceea */
    @Test
    void updateGradeValueTest() throws Exception {
        // Setup
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(grade);

        String evalDateTest = "12.12.2012";
        Integer valueTest = 3;

        when(studentsService.updateGrade(eq(student.getId()), any(Integer.class), anyString(), eq(grade.getId()))).thenAnswer((Answer<Grade>) invocationOnMock -> {
            Grade toUpdate = student.getGradeById(grade.getId());
            toUpdate.setEvaluationDate(evalDateTest);
            toUpdate.setValue(valueTest);
            return toUpdate;
        });
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

        assertNotNull(gradeResponse);
        // assertEquals(gradeResponse.getId(), grade.getId());
        // assertEquals("12.12.2002", gradeResponse.getEvaluationDate());

        // Test case 2: when student is not present
        when(studentsService.getStudentById(student.getId())).thenReturn(null);

        updateGradeResult = mvc.perform(put("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .param("evaluationDateMs", asJsonString(evalDateTest))
                        .param("value", asJsonString(valueTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("", updateGradeResult.getResponse().getContentAsString());

        // Test case 3: when grade is not present
        when(studentsService.getStudentById(student.getId())).thenReturn(student);
        when(studentsService.getGradeById(student.getId(), grade.getId())).thenReturn(null);

        updateGradeResult = mvc.perform(put("/api/v1/students/{id}/grades/{gradeId}", student.getId(), grade.getId())
                        .param("evaluationDateMs", asJsonString(evalDateTest))
                        .param("value", asJsonString(valueTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

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