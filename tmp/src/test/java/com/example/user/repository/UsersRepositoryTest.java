package com.example.user.repository;

import com.example.security.objects.Student;
import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DirtiesContext
    void findUsersByParamsEmailExistsTest() {
        //
        //Given
        //
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

        usersRepository.save(student);
        //
        //When
        //
        String email = "testemail@mail.com";

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
        String email = "testemail@ail.com";
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
    @DirtiesContext
    void findUsersByParamsUsernameExistsTest() {
        //
        //Given
        //
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

        usersRepository.save(student);
        //
        //When
        //
        String username = "testUser";

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
        String username = "tesUser";
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