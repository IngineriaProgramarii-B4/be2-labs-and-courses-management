package com.example.user.repository;

import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminsRepositoryTest {
    @Autowired
    private AdminsRepository adminsRepository;

    Admin admin;

    @Test
    @DirtiesContext
    @Rollback
    void findAdminsByParamsEmailExistsTest() {

        //
        //Given
        //
        admin = new Admin(
                UUID.randomUUID(),
                "testName",
                "testSurname",
                "testemail@mail.com",
                "testUser",
                "testOxgvcice",
                "testDepartment",
                "747f61f6-f24f-11ed-a05b-0242ac120003"
        );

        List<Admin> expected = List.of(admin);
        adminsRepository.save(admin);

        //
        //When
        //
        String email = "testemail@mail.com";

        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                email,
                null,
                null,
                null
        );

        //
        //Then
        //
        assertEquals(true, result.containsAll(expected));

    }

    @Test
    void findAdminsByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "testmail@mail.com";
        //
        //When
        //
        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                email,
                null,
                null,
                null
        );
        //
        //Then
        //
        assertTrue(result.isEmpty());
    }

    @Test
    @DirtiesContext
    @Rollback
    void findAdminsByParamsUsernameExistsTest() {
        //
        //Given
        //
        admin = new Admin(
                UUID.randomUUID(),
                "testName",
                "testSurname",
                "testemail@mail.com",
                "testUser",
                "testOffice",
                "testDepartment",
                "747f61f6-f24f-11ed-a05b-0242ac120003"

        );

        adminsRepository.save(admin);

        //When
        String username = "testUser";

        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                null,
                username,
                null,
                null
        );

        //Then
        assertTrue(result.contains(admin));
    }

    @Test
    void findAdminsByParamsUsernameNonexistentTest() {

        //Given
        String username = "testUsef";
        //When
        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                null,
                username,
                null,
                null
        );

        //Then
        assertTrue(result.isEmpty());
    }
}