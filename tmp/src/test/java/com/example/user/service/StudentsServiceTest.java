package com.example.user.service;

import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.security.services.StudentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudentsServiceTest {

    @InjectMocks
    StudentsService studentsService;

    @Mock
    StudentsRepository studentsRepository;

    private Student student;

    private boolean simulateRepo(Map<String, Object> map) {
        List<Student> expected = List.of(student);

        given(studentsRepository.findStudentsByParams(
                !map.containsKey("id") ? nullable(UUID.class) : (UUID) eq(map.get("id")),
                !map.containsKey("firstname") ? nullable(String.class) : (String) eq(map.get("firstname")),
                !map.containsKey("lastname") ? nullable(String.class) : (String) eq(map.get("lastname")),
                !map.containsKey("email") ? nullable(String.class) : (String) eq(map.get("email")),
                !map.containsKey("username") ? nullable(String.class) : (String) eq(map.get("username")),
                !map.containsKey("year") ? eq(0) : anyInt(),
                !map.containsKey("semester") ? eq(0) : anyInt(),
                !map.containsKey("registrationNumber") ? nullable(String.class) : (String) eq(map.get("registrationNumber"))))
                .willReturn(List.of(student));

        List<Student> result = studentsService.getStudentsByParams(map);

        return result.equals(expected) ? true : false;
    }

    @BeforeEach
    public void setup() {
        student = new Student(
                "FirstnameTest",
                "LastnameTest",
                "EmailTest@gmail.com",
                "UsernameTest",
                1,
                2,
                "RegNumberTest",
                null
        );
    }

    @Test
    void getStudentsByParamsYearTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("year", "1");
        args.put("semester", "");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getStudentsByParamsSemesterTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("semester", "2");
        args.put("id", UUID.randomUUID());
        args.put("year", "");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getStudentsByParamsRegistrationNumberTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("registrationNumber", "RegNumberTest");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getStudentsByParamsIdTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        UUID idTest = UUID.randomUUID();
        args.put("id", idTest);

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void saveStudentTest() {
        //When
        when(studentsRepository.save(student)).thenReturn(student);

        studentsService.saveStudent(student);

        //Then
        verify(studentsRepository, times(1)).save(student);
    }

    @Test
    void updateStudentTest() {
        //When
        doNothing().when(studentsRepository).updateStudent(student.getId(), student.getFirstname(), student.getLastname(), student.getEmail(), student.getUsername(), student.getYear(), student.getSemester(), student.getRegistrationNumber());

        studentsService.updateStudent(student.getId(), student);

        //Then
        verify(studentsRepository, times(1)).updateStudent(student.getId(), student.getFirstname(), student.getLastname(), student.getEmail(), student.getUsername(), student.getYear(), student.getSemester(), student.getRegistrationNumber());
    }
}