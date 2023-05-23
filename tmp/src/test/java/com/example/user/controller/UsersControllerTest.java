package com.example.user.controller;
import com.example.security.objects.Admin;
import com.example.security.objects.Student;
import com.example.security.objects.Teacher;
import com.example.security.objects.User;
import com.example.security.services.UsersService;
import com.example.user.controllers.UsersController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    private Student stud1, stud2, stud3;
    private Teacher teacher1;
    private Admin admin1;

    @BeforeEach
    public void setup() {

        stud1 = new Student(
                "Florin",
                "Rotaru",
                "florin.eugen@uaic.ro",
                "florin02",
                2,
                4,
                "123FAKE92929",
                null);

        teacher1 = new Teacher(
                "Ciobaca",
                "Stefan",
                "stefan.ciobaca@uaic.com",
                "stefan.ciobaca",
                "C401",
                "12345",
                null,
                "Prof");

        stud2 = new Student(
                "Antip",
                "Andrei",
                "andrei.antip@uaic.ro",
                "andreiul",
                1,
                2,
                "123BOSS135",
                null);

        stud3 = new Student(
                "Olariu",
                "Andreea",
                "andreea.olariu@uaic.ro",
                "andreea.olariu",
                2,
                1,
                "12300000GSGVGDS1",
                null);

        admin1 = new Admin(
                "Cuzic",
                "Diana",
                "diana.cuzic@gmail.com",
                "dianacuzic",
                "P1",
                "Secretariat",
                "132456");
    }

    @Test
    void getUsersByParamsTest() throws Exception {
        //Given
        String url = "/api/v1/users";

        List<User> listUsers = List.of(stud1, stud2, stud3, admin1, teacher1);

        Map<String, Object> args = Collections.emptyMap();

        //When
        when(usersService.getUsersByParams(args)).thenReturn(listUsers);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listUsers);

        } catch(JsonProcessingException e) {
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
        } catch(JsonProcessingException e) {
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

}
