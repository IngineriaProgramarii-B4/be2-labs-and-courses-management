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
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class AdminsRepositoryTest{

    @Autowired
    private AdminsRepository adminsRepository;
    protected boolean findInRepo(Map<String, Object> map)
    {
        var admin = adminsRepository.findAdminsByParams(
                (UUID) map.get("id"),
                (String)map.get("firstname"),
                (String)map.get("lastname"),
                (String)map.get("email"),
                (String)map.get("username"),
                (String)map.get("office"),
                (String)map.get("department")

        );
        return (!admin.isEmpty());
    }

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
        var exists = findInRepo(Map.of("email", email));

        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("email", email));

        //
        //Then
        //
        assertFalse(exists);
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
        var exists = findInRepo(Map.of("username", username));
        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("username", username));

        //
        //Then
        //
        assertFalse(exists);
    }
}