package com.example.user.controller;

import com.example.security.objects.Admin;
import com.example.security.objects.Student;
import com.example.security.objects.Teacher;
import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import com.example.security.services.UsersService;
import com.example.signin.model.Role;
import com.example.signin.security.JWTGenerator;
import com.example.subject.model.Subject;
import com.example.user.controllers.UsersController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Mock
    UsersRepository usersRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    private Student stud1, stud2, stud3, stud4;
    private Teacher teacher1;
    private Admin admin1;

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
                "708ba4d2-f250-11ed-a05b-0242ac120003",
                new HashSet<>(Arrays.asList(new Subject())));

        teacher1 = new Teacher(UUID.randomUUID(),
                "Ciobaca",
                "Stefan",
                "stefan.ciobaca@uaic.com",
                "stefan.ciobaca",
                "C401",
                new HashSet<>(Arrays.asList(new Subject())),
                "Prof",
                "9f158cc8-f250-11ed-a05b-0242ac120003");

        stud2 = new Student(UUID.randomUUID(),
                "Antip",
                "Andrei",
                "andrei.antip@uaic.ro",
                "andreiul",
                1,
                2,
                "b14ac87c-f250-11ed-a05b-0242ac120003",
                new HashSet<>(Arrays.asList(new Subject()))
        );

        stud3 = new Student(UUID.randomUUID(),
                "Olariu",
                "Andreea",
                "andreea.olariu@uaic.ro",
                "andreea.olariu",
                2,
                1,
                "a9810502-f250-11ed-a05b-0242ac120003",
                new HashSet<>(Arrays.asList(new Subject())));

        stud4 = new Student(UUID.randomUUID(),
                "Olariu",
                "Andreea",
                "andreeaolariu13@gmail.com",
                "andreeaolariu13",
                2,
                1,
                "224342",
                new HashSet<>(Arrays.asList(new Subject())));


        admin1 = new Admin(UUID.randomUUID(),
                "Cuzic",
                "Diana",
                "diana.cuzic@gmail.com",
                "dianacuzic",
                "P1",
                "Secretariat",
                "b6e1fb8e-f250-11ed-a05b-0242ac120003");
    }

    @AfterClass
    void clean() {
        usersRepository.delete(stud1);
        usersRepository.delete(stud2);
        usersRepository.delete(stud3);
        usersRepository.delete(stud4);
        usersRepository.delete(teacher1);
        usersRepository.delete(admin1);
    }

    @Test
    void getUsersByParamsTest() throws Exception {

        //Given
        String url = "/api/v1/users";

        List<User> listUsers = List.of(stud1, stud2, stud3, admin1, teacher1);

        Map<String, Object> args = Collections.emptyMap();

        //When
        when(usersService.getUsersByParams(args)).thenReturn(listUsers);

        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listUsers);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getUsersByParamsUsernameTest() throws Exception {


        //Given
        String url = "/api/v1/users?username=andreea.olariu";

        List<User> listUsers = List.of(stud3);

        Map<String, Object> args = new HashMap<>();

        args.put("username", "andreea.olariu");

        //When
        when(usersService.getUsersByParams(args)).thenReturn(listUsers);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listUsers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getUsersByParamsNonExisting() throws Exception {
        //Given
        String url = "/api/v1/users?username=NonExisting";

        //When
        when(usersService.getUsersByParams(Map.of("username", "NonExisting"))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(url)).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

//    @Test
//    void getLoggedUserTest() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        JWTGenerator jwtGenerator = new JWTGenerator();
//
//        //Given
//        given(usersRepository.findUsersByParams(
//                nullable(UUID.class),
//                nullable(String.class),
//                nullable(String.class),
//                eq(stud4.getEmail()),
//                nullable(String.class)))
//                .willReturn(List.of(stud1));
//
//        when(usersService.getUsersByParams(Map.of("email", stud4.getEmail()))).thenReturn(List.of(stud4));
//
//        String url = "/api/v1/users/loggedUser";
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbmRyZWVhb2xhcml1MTNAZ21haWwuY29tIiwicm9sZSI6IlRFQUNIRVIiLCJpYXQiOjE2ODQyMjQ1NDEsImV4cCI6MTY4NDIyODE0MX0.cJBzslc28_iKvKfj01fC_kr70RvBfFwheB7XquXe-7ybZW0jZYdB4-BXlbggotaOKwyKXi6op10YtsWOfMpH1g";
//
//        //When
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url).content(token)).andReturn();
//
//        int result = mvcResult.getResponse().getStatus();
//
//        //Then
//        assertEquals(HttpStatus.OK.value(), result);
//
//        String value = null;
//        try {
//            value = jwtGenerator.getEmailFromJWT(token);
//        } catch (RuntimeException e) {
//            // Use assertThrows to catch the exception and verify its type and message
//            Exception exception = assertThrows(Exception.class, () -> jwtGenerator.getEmailFromJWT(token));
//            String actualMessage = exception.getMessage();
//            String expectedMessage = "An error occurred at object mapping";
//            assertEquals(expectedMessage, actualMessage);
//
//        }
//        assertEquals(stud4.getEmail(), value);
//    }

//    @Test
//    void getLoggedUserNotFoundTest() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbmRyZWZzZWQuY29tIiwicm9sZSI6IlRFQUNIRVIiLCJpYXQiOjE2ODQyMjQ1NDEsImV4cCI6MTY4NDIyODE0MX0.PW65cDIaWm0rXCyl5duVPhjzCpB53YRc18vMN4ikaeqcwaNIRwvHkoaOqRbmPLTtGop71U7wWFbuwIMUHu4E_g";
//        String url = "/api/v1/users/loggedUser";
//        // Act
//        ResponseEntity<User> response = usersController.getLoggedUser(token);
//
//        // Assert
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(null, response.getBody());
//        verify(usersService, never()).getUsersByParams(anyMap());
//
//    }

}
