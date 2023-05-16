package com.example.user.controller;

import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.security.services.StudentsService;
import com.example.security.services.UsersService;
import com.example.signin.controllers.AuthController;
import com.example.signin.dto.LoginRequestBody;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.repository.CredentialsRepository;
import com.example.signin.security.EmailService;
import com.example.signin.security.JWTGenerator;
import com.example.signin.service.AdminService;
import com.example.signin.service.CredentialsService;
import com.example.signin.service.StudentService;
import com.example.signin.service.TeacherService;
import com.example.signin.repository.RoleRepository;
import com.example.subject.model.Subject;
import com.example.user.controllers.StudentsController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentsService studentsService;

    @Mock
    private StudentsRepository studentsRepository;

    private Student stud1, stud2, stud3;

    @BeforeEach
    public void setup() {
        stud1 = new Student(
                UUID.randomUUID(),
                "Florin",
                "Rotaru",
                "florin.eugen@uaic.ro",
                "florin02",
                2,
                4,
                "123e4567-e89b-12d3-a456-426614174000",
                new HashSet<>(Arrays.asList(new Subject())));
        stud2 = new Student(
                UUID.randomUUID(),
                "Antip",
                "Andrei",
                "andrei.antip@uaic.ro",
                "andreiul",
                1,
                2,
                "a498dcec-f24e-11ed-a05b-0242ac120003",
                new HashSet<>(Arrays.asList(new Subject())));
        stud3 = new Student(
                UUID.randomUUID(),
                "Olariu",
                "Andreea",
                "andreea.olariu@uaic.ro",
                "andreea.olariu",
                2,
                1,
                "af695692-f24e-11ed-a05b-0242ac120003",
                new HashSet<>(Arrays.asList(new Subject())));
    }

    @AfterClass
    public void clean(){
        studentsRepository.delete(stud1);
        studentsRepository.delete(stud2);
        studentsRepository.delete(stud3);
    }

    @Test
    void getStudentsByParamsTest() throws Exception {
        //Given
        String url = "/api/v1/students";

        List<Student> listStudents = List.of(stud1, stud2, stud3);

        Map<String, Object> args = Collections.emptyMap();

        when(studentsService.getStudentsByParams(args)).thenReturn(listStudents);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listStudents);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(result, expected);
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

        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listStudents);
        } catch (JsonProcessingException e) {
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
