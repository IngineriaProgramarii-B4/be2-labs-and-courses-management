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
        List<User> result = usersRepository.findUsersByParams(
                null,
                null,
                null,
                email,
                null);

        //
        //Then
        //
        assertTrue(result.contains(student));
    }

    @Test
    void findUsersByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "nonexistent@mail.com";

        //
        //When
        //
        List<User> result = usersRepository.findUsersByParams(
                null,
                null,
                null,
                email,
                null);

        //
        //Then
        //
        assertTrue(result.isEmpty());
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
        List<User> result = usersRepository.findUsersByParams(
                null,
                null,
                null,
                null,
                username);

        //
        //Then
        //
        assertTrue(result.contains(student));
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
        List<User> result = usersRepository.findUsersByParams(
                null,
                null,
                null,
                null,
                username);

        //
        //Then
        //
        assertTrue(result.isEmpty());
    }
}