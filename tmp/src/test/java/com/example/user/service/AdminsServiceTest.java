package com.example.user.service;

import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import com.example.security.services.AdminsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminsServiceTest {

    @InjectMocks
    AdminsService adminsService;

    @Mock
    AdminsRepository adminsRepository;

    private Admin admin;

    private boolean simulateRepo(Map<String, Object> map) {
        List<Admin> expected = List.of(admin);

        given(adminsRepository.findAdminsByParams(
                !map.containsKey("id") ? nullable(UUID.class) : (UUID) eq(map.get("id")),
                !map.containsKey("firstname") ? nullable(String.class) : (String) eq(map.get("firstname")),
                !map.containsKey("lastname") ? nullable(String.class) : (String) eq(map.get("lastname")),
                !map.containsKey("email") ? nullable(String.class) : (String) eq(map.get("email")),
                !map.containsKey("username") ? nullable(String.class) : (String) eq(map.get("username")),
                !map.containsKey("office") ? nullable(String.class) : (String) eq(map.get("office")),
                !map.containsKey("department") ? nullable(String.class) : (String) eq(map.get("department"))))
                .willReturn(List.of(admin));

        List<Admin> result = adminsService.getAdminsByParams(map);

        return result.equals(expected) ? true : false;
    }

    @BeforeEach
    public void setup() {
        admin = new Admin(
                "FirstnameTest",
                "LastnameTest",
                "EmailTest@gmail.com",
                "UsernameTest",
                "OfficeTest",
                "DepartmentTest",
                "12345"
        );
    }

    @Test
    void getAdminsByParamsOfficeTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("office", "OfficeTest");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getAdminsByParamsDepartmentTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("department", "DepartmentTest");
        args.put("id", UUID.randomUUID());

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getAdminsByParamsIdTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        UUID idTest = UUID.randomUUID();
        args.put("id", idTest);

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void saveAdminTest() {
        //When
        when(adminsRepository.save(admin)).thenReturn(admin);
        adminsService.saveAdmin(admin);

        //Then
        verify(adminsRepository, times(1)).save(admin);
    }

    @Test
    void updateAdminTest() {
        //When
        doNothing().when(adminsRepository).updateAdmin(admin.getId(), admin.getFirstname(), admin.getLastname(), admin.getEmail(), admin.getUsername(), admin.getOffice(), admin.getDepartment());

        adminsService.updateAdmin(admin.getId(), admin);

        //Then
        verify(adminsRepository, times(1)).updateAdmin(admin.getId(), admin.getFirstname(), admin.getLastname(), admin.getEmail(), admin.getUsername(), admin.getOffice(), admin.getDepartment());
    }
}