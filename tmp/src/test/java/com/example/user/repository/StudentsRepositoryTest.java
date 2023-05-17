package com.example.user.repository;

import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentsRepositoryTest {

    @Autowired
    private StudentsRepository studentsRepository;

    private Student student = new Student(
            "testName",
            "testSurename",
            "testemail@mail.com",
            "testUser",
            1,
            1,
            "testRegistrationNumber",
            null
    );

    @BeforeEach
    public void setup() {
        studentsRepository.save(student);
    }

    @AfterEach
    public void cleanup() {
        studentsRepository.delete(student);
    }

    @Test
    void findStudentsByParamsEmailExistsTest() {
        //
        //Given
        //
        String email = "testemail@mail.com";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                email,
                null,
                0,
                0,
                null
        );

        //
        //Then
        //
        assertTrue(result.contains(student));
    }

    @Test
    void findStudentsByParamsEmailNonexistentTest() {
        //
        //Given
        //
        String email = "nonexistent@mail.com";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                email,
                null,
                0,
                0,
                null
        );

        //
        //Then
        //
        assertTrue(result.isEmpty());
    }

    @Test
    @DirtiesContext
    void findStudentsByParamsUsernameExistsTest() {
        //
        //Given
        //
        String username = "testUser";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                null,
                username,
                0,
                0,
                null
        );

        //
        //Then
        //
        assertTrue(result.contains(student));
    }

    @Test
    void findStudentsByParamsUsernameNonexistentTest() {
        //
        //Given
        //
        String username = "nonexisetntUsername";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                null,
                username,
                0,
                0,
                null
        );

        //
        //Then
        //
        assertTrue(result.isEmpty());
    }

    @Test
    @DirtiesContext
    void findStudentsByParamsRegistrationNumberExistsTest() {
        //
        //Given
        //
        String regisNr = "testRegistrationNumber";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                null,
                null,
                1,
                1,
                regisNr
        );

        //
        //Then
        //
        assertTrue(result.contains(student));
    }

    @Test
    void findStudentsByParamsRegistrationNumberNonexistentTest() {
        //
        //Given
        //
        String regisNr = "nonexistentRegistrationNumber";

        //
        //When
        //
        List<Student> result = studentsRepository.findStudentsByParams(
                null,
                null,
                null,
                null,
                null,
                1,
                1,
                regisNr
        );

        //
        //Then
        //
        assertTrue(result.isEmpty());
    }
}