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
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeachersRepositoryTest {

    @Autowired
    private TeachersRepository teachersRepository;
    protected boolean findInRepo(Map<String, Object> map)
    {
        var teacher = teachersRepository.findTeachersByParams(
                (UUID) map.get("id"),
                (String)map.get("firstname"),
                (String)map.get("lastname"),
                (String)map.get("email"),
                (String)map.get("username"),
                (String)map.get("office"),
                (String)map.get("title")
        );
        return (!teacher.isEmpty());
    }


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
        var exists = findInRepo(Map.of("email", email));
        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("email", email));
        //
        //Then
        //
        assertFalse(exists);
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
        var exists = findInRepo(Map.of("username", username));
        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("username", username));
        //
        //Then
        //
        assertFalse(exists);
    }
}