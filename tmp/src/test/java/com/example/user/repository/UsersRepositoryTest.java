package com.example.user.repository;

import com.example.security.objects.Student;
import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
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
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    Student student = new Student(
            "testName",
            "testSurname",
            "testemail@mail.com",
            "testUser",
            1,
            1,
            "testRegistrationNumber",
            null
    );

    protected boolean findInRepo(Map<String, Object> map)
    {
        var user = usersRepository.findUsersByParams(
                (UUID) map.get("id"),
                (String)map.get("firstname"),
                (String)map.get("lastname"),
                (String)map.get("email"),
                (String)map.get("username")
        );
        return (!user.isEmpty());
    }

    @BeforeEach
    public void setup() {
        usersRepository.save(student);
    }

    @AfterEach
    public void cleanup() {
        usersRepository.delete(student);
    }

    @Test
    void findUsersByParamsEmailExistsTest() {
        //
        //Given
        //
        String email = "testemail@mail.com";
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
    void findUsersByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "nonexistent@gmail.com";
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
    void findUsersByParamsUsernameExistsTest() {
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
    void findUsersByParamsUsernameNonexistentTest() {
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