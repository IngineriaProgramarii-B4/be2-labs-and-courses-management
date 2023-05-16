package com.example.user.repository;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class TeachersRepositoryTest {

    @Autowired
    private TeachersRepository teachersRepository;
    Teacher teacher;

    @Test
    @DirtiesContext
    void findTeachersByParamsEmailExistsTest() {
        //
        //Given
        //
        teacher = new Teacher(
                UUID.randomUUID(),
                "testName",
                "testSurname",
                "testemail@mail.com",
                "testUser",
                "testOffice",
                null,
                "testTitle",
                "708ba4d2-f250-11ed-a05b-0242ac120003"
        );

        teachersRepository.save(teacher);
        //
        //When
        //
        String email = "testemail@mail.com";

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
        String email = "testemail@gmail.com";
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
    @DirtiesContext
    void findTeachersByParamsUsernameExistsTest() {
        //
        //Given
        //
        Teacher teacher = new Teacher(
                UUID.randomUUID(),
                "testName",
                "testSurname",
                "testemail@mail.com",
                "testUser",
                "testOffice",
                null,
                "testTitle",
                "708ba4d2-f250-11ed-a05b-0242ac120003"
        );

        teachersRepository.save(teacher);
        //
        //When
        //
        String username = "testUser";

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
        String username = "testUsr";
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