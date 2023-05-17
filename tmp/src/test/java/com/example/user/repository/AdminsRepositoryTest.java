package com.example.user.repository;

import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class AdminsRepositoryTest {

    @Autowired
    private AdminsRepository adminsRepository;

    private final Admin admin = new Admin(
            "testName",
            "testSurname",
            "testemail@mail.com",
            "testUser",
            "testOffice",
            "testDepartment",
            "234"
    );

    @BeforeEach
    public void setup() {
        adminsRepository.save(admin);
    }

    @AfterEach
    public void cleanup() {
        adminsRepository.delete(admin);
    }

    @Test
    void findAdminsByParamsEmailExistsTest() {
        //
        //Given
        //
        String email = "testemail@mail.com";

        List<Admin> expected = List.of(admin);

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

        //Then
        //
        assertEquals(true, result.containsAll(expected));
    }

    @Test
    void findAdminsByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "nonexistent@mail.com";

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
    void findAdminsByParamsUsernameExistsTest() {
        //
        //Given
        //
        String username = "testUser";

        //
        //When
        //
        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                null,
                username,
                null,
                null
        );

        //
        //Then
        //
        assertTrue(result.contains(admin));
    }

    @Test
    void findAdminsByParamsUsernameNonexistentTest() {
        //
        //Given
        //
        String username = "nonexistentUsername";

        //
        //When
        //
        List<Admin> result = adminsRepository.findAdminsByParams(
                null,
                null,
                null,
                null,
                username,
                null,
                null
        );

        //
        //Then
        //
        assertTrue(result.isEmpty());
    }
}