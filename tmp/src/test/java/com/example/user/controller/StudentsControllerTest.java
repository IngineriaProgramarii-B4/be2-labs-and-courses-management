package com.example.user.controller;
import com.example.security.objects.Student;
import com.example.security.services.StudentsService;
import com.example.subject.service.SubjectService;
import com.example.user.controllers.StudentsController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentsService studentsService;
    @MockBean
    private SubjectService subjectService;

    private Student stud1, stud2, stud3;

    @BeforeEach
    public void setup() {
        stud1 = new Student(
                UUID.fromString("a838ae68-f4b0-11ed-a05b-0242ac120003"),
                "Florin",
                "Rotaru",
                "florin.eugen@uaic.ro",
                "florin02",
                2,
                4,
                "123FAKE92929",
                null);
        stud2 = new Student(
                UUID.fromString("c6a41a36-f4b0-11ed-a05b-0242ac120003"),
                "Antip",
                "Andrei",
                "andrei.antip@uaic.ro",
                "andreiul",
                1,
                2,
                "123BOSS135",
                null);
        stud3 = new Student(
                UUID.fromString("caa30a3e-f4b0-11ed-a05b-0242ac120003"),
                "Olariu",
                "Andreea",
                "andreea.olariu@uaic.ro",
                "andreea.olariu",
                2,
                1,
                "12300000GSGVGDS1",
                null);
    }

    @Test
    void getStudentsByParamsTest() throws Exception {
        //Given
        String url = "/api/v1/students";

        List<Student> listStudents = List.of(stud1, stud2, stud3);

        Map<String, Object> args = Collections.emptyMap();

        //When
        when(studentsService.getStudentsByParams(args)).thenReturn(listStudents);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listStudents);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getStudentsByParamsYearTest() throws Exception {
        //Given
        String url = "/api/v1/students?year=2";

        List<Student> listStudents = List.of(stud1, stud3);

        Map<String, Object> args = new HashMap<>();

        args.put("year", "2");

        //When
        when(studentsService.getStudentsByParams(args)).thenReturn(listStudents);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listStudents);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getStudentsByParamsNonExisting() throws Exception {
        //Given
        String url = "/api/v1/students?lastname=NonExisting";

        //When
        when(studentsService.getStudentsByParams(Map.of("lastname", "NonExisting"))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(url)).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    @Test
    void updateStudentTest() throws Exception {
        //Given
        String url = "/api/v1/student/" + stud1.getId();

        //When
        when(studentsService.getStudentsByParams(Map.of("id", stud1.getId()))).thenReturn(List.of(stud1));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(stud1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NO_CONTENT.value(), result);
    }

    @Test
    void updateStudentNonExistentTest() throws Exception {
        //Given
        String url = "/api/v1/student/" + stud1.getId();

        //When
        when(studentsService.getStudentsByParams(Map.of("id", stud1.getId()))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(stud1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    @Test
    void saveStudentTest() throws Exception {
        //Given
        String url = "/api/v1/students";

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(stud1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.CREATED.value(), result);
    }

    String ObjectToJson(Student student) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(student);
    }
}
