package com.example.user.controller;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.security.services.TeachersService;
import com.example.subject.model.Subject;
import com.example.user.controllers.TeachersController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeachersController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeachersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeachersService teachersService;

    @Mock
    private TeachersRepository teachersRepository;

    private Teacher teacher1;

    @BeforeEach
    public void setup() {
        teacher1 = new Teacher(
                UUID.randomUUID(),
                "Ciobaca",
                "Stefan",
                "stefan.ciobaca@uaic.com",
                "stefan.ciobaca",
                "C401",
                new HashSet<>(Arrays.asList(new Subject())),
                "Prof",
                "8e93c300-f251-11ed-a05b-0242ac120003");
    }

    void clean() {
        teachersRepository.delete(teacher1);
    }

    @Test
    void getTeachersByParamsTest() throws Exception {
        //Given
        String url = "/api/v1/teachers";

        List<Teacher> listTeachers = List.of(teacher1);

        Map<String, Object> args = Collections.emptyMap();

        //When
        when(teachersService.getTeachersByParams(args)).thenReturn(listTeachers);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listTeachers);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getTeachersByParamsTitleTest() throws Exception {
        //Given
        String url = "/api/v1/teachers?title=Prof";

        List<Teacher> listTeachers = List.of(teacher1);

        Map<String, Object> args = new HashMap<>();

        args.put("title", "Prof");

        //When
        when(teachersService.getTeachersByParams(args)).thenReturn(listTeachers);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listTeachers);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getTeachersByParamsNonExisting() throws Exception {
        //Given
        String url = "/api/v1/teachers?title=NonExisting";

        //When
        when(teachersService.getTeachersByParams(Map.of("title", "NonExisting"))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(url)).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    @Test
    void updateTeacherTest() throws Exception {
        //Given
        String url = "/api/v1/teacher/" + teacher1.getId();

        //When
        when(teachersService.getTeachersByParams(Map.of("id", teacher1.getId()))).thenReturn(List.of(teacher1));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(teacher1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NO_CONTENT.value(), result);
    }

    @Test
    void updateTeacherNonExistentTest() throws Exception {
        //Given
        String url = "/api/v1/teacher/" + teacher1.getId();

        //When
        when(teachersService.getTeachersByParams(Map.of("id", teacher1.getId()))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(teacher1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    @Test
    void saveTeacherTest() throws Exception {
        //Given
        String url = "/api/v1/teachers";

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(teacher1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.CREATED.value(), result);
    }

    String ObjectToJson(Teacher teacher) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(teacher);
    }
}
