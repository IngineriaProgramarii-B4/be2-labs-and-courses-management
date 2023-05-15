package com.example.user.controller;

import com.example.security.objects.Admin;
import com.example.security.services.AdminsService;
import com.example.user.controllers.AdminsController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(AdminsController.class)
class AdminsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminsService adminsService;

    private Admin admin1;

    @BeforeEach
    public void setup() {

        admin1 = new Admin(
                UUID.randomUUID(),
                "Cuzic",
                "Diana",
                "diana.cuzic@gmail.com",
                "dianacuzic",
                "P1",
                "Secretariat",
                "36fbe822-f24f-11ed-a05b-0242ac120003");
    }

    @Test
    void getAdminsByParamsTest() throws Exception {
        //Given
        String url = "/api/v1/admins";

        List<Admin> listAdmins = List.of(admin1);

        Map<String, Object> args = Collections.emptyMap();

        //When
        when(adminsService.getAdminsByParams(args)).thenReturn(listAdmins);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listAdmins);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getAdminsByParamsDepartmentTest() throws Exception {
        //Given
        String url = "/api/v1/admins?department=Secretariat";

        List<Admin> listAdmins = List.of(admin1);

        Map<String, Object> args = new HashMap<>();
        args.put("department", "Secretariat");

        //When
        when(adminsService.getAdminsByParams(args)).thenReturn(listAdmins);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        //Then
        String expected = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            expected = objectMapper.writeValueAsString(listAdmins);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    @Test
    void getAdminsByParamsNonExisting() throws Exception {
        //Given
        String url = "/api/v1/admins?department=NonExisting";

        //When
        when(adminsService.getAdminsByParams(Map.of("department", "NonExisting"))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(url)).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    @Test
    void saveAdminTest() throws Exception {
        //Given
        String url = "/api/v1/admins";

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(admin1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.CREATED.value(), result);
    }

    @Test
    void updateAdminTest() throws Exception {
        //Given
        String url = "/api/v1/admin/" + admin1.getId();

        //When
        when(adminsService.getAdminsByParams(Map.of("id", admin1.getId()))).thenReturn(List.of(admin1));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(admin1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NO_CONTENT.value(), result);
    }

    @Test
    void updateAdminNonExistentTest() throws Exception {
        //Given
        String url = "/api/v1/admin/" + admin1.getId();

        //When
        when(adminsService.getAdminsByParams(Map.of("id", admin1.getId()))).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(ObjectToJson(admin1))).andReturn();

        int result = mvcResult.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result);
    }

    String ObjectToJson(Admin admin) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(admin);
    }
}