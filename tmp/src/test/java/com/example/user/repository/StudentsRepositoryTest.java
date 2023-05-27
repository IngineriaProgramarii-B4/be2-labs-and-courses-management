package com.example.user.repository;

import com.example.security.objects.Student;
import com.example.security.repositories.AdminsRepository;
import com.example.security.repositories.StudentsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentsRepositoryTest {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    protected boolean findInRepo(Map<String, Object> map)
    {
        var student = studentsRepository.findStudentsByParams(
                (UUID) map.get("id"),
                (String)map.get("firstname"),
                (String)map.get("lastname"),
                (String)map.get("email"),
                (String)map.get("username"),
                map.get("year") == null ? 0 : (Integer)map.get("year"),
                map.get("semester") == null ? 0 : (Integer)map.get("semester"),
                (String)map.get("registrationNumber")
        );
        return (!student.isEmpty());
    }

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
        var exists = findInRepo(Map.of("email", email));

        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("email", email));

        //
        //Then
        //
        assertFalse(exists);
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
        var exists = findInRepo(Map.of("username", username));
        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("username", username));
        //
        //Then
        //
        assertFalse(exists);
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

        var exists = findInRepo(Map.of("registrationNumber", regisNr));
        //
        //Then
        //
        assertTrue(exists);
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
        var exists = findInRepo(Map.of("registrationNumber", regisNr));
        //
        //Then
        //
        assertFalse(exists);
    }
}