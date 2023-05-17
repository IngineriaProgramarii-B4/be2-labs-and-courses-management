package com.example.user.repository;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeachersRepositoryTest {

    @Autowired
    private TeachersRepository teachersRepository;

    private Teacher teacher = new Teacher(
            "testName",
            "testSurname",
            "testemail@mail.com",
            "testUser",
            "testOffice",
            "1234",
            null,
            "testTitle"
    );

    @BeforeEach
    public void setup() {
        teachersRepository.save(teacher);
    }

    @AfterEach
    public void cleanup() {
        teachersRepository.delete(teacher);
    }

    @Test
    void findTeachersByParamsEmailExistsTest() {
        //
        //Given
        //
        String email = "testemail@mail.com";

        //
        //When
        //
        List<Teacher> result = teachersRepository.findTeachersByParams(
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
        assertTrue(result.contains(teacher));
    }

    @Test
    void findTeachersByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "nonexistent@gmail.com";

        //
        //When
        //
        List<Teacher> result = teachersRepository.findTeachersByParams(
                null,
                null,
                email,
                null,
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
    void findTeachersByParamsUsernameExistsTest() {
        //
        //Given
        //
        String username = "testUser";

        //
        //When
        //
        List<Teacher> result = teachersRepository.findTeachersByParams(
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
        assertTrue(result.contains(teacher));
    }

    @Test
    void findTeachersByParamsUsernameNonexistentTest() {
        //
        //Given
        //
        String username = "nonexistentUsername";

        //
        //When
        //
        List<Teacher> result = teachersRepository.findTeachersByParams(
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