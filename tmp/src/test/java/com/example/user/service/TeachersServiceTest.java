package com.example.user.service;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.security.services.TeachersService;
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
class TeachersServiceTest {

    @InjectMocks
    TeachersService teachersService;

    @Mock
    TeachersRepository teachersRepository;

    private Teacher teacher;

    private boolean simulateRepo(Map<String, Object> map) {
        List<Teacher> expected = List.of(teacher);

        given(teachersRepository.findTeachersByParams(
                !map.containsKey("id") ? nullable(UUID.class) : (UUID) eq(map.get("id")),
                !map.containsKey("firstname") ? nullable(String.class) : (String) eq(map.get("firstname")),
                !map.containsKey("lastname") ? nullable(String.class) : (String) eq(map.get("lastname")),
                !map.containsKey("email") ? nullable(String.class) : (String) eq(map.get("email")),
                !map.containsKey("username") ? nullable(String.class) : (String) eq(map.get("username")),
                !map.containsKey("office") ? nullable(String.class) : (String) eq(map.get("office")),
                !map.containsKey("title") ? nullable(String.class) : (String) eq(map.get("title"))))
                .willReturn(List.of(teacher));

        List<Teacher> result = teachersService.getTeachersByParams(map);

        return result.equals(expected) ? true : false;
    }

    @BeforeEach
    public void setup() {
        teacher = new Teacher(
                "FirstnameTest",
                "LastnameTest",
                "EmailTest@gmail.com",
                "UsernameTest",
                "OfficeTest",
                "12345",
                null,
                "TitleTest"
        );
    }

    @Test
    void getTeachersByParamsOfficeTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("office", "OfficeTest");
        args.put("id", UUID.randomUUID());

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getTeachersByParamsTitleTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("title", "TitleTest");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getTeachersByParamsIdTest() {
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
    void saveTeacherTest() {
        //When
        when(teachersRepository.save(teacher)).thenReturn(teacher);

        teachersService.saveTeacher(teacher);

        //Then
        verify(teachersRepository, times(1)).save(teacher);
    }

    @Test
    void updateTeacherTest() {
        //When
        doNothing().when(teachersRepository).updateTeacher(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), teacher.getEmail(), teacher.getUsername(), teacher.getOffice(), teacher.getTitle());

        teachersService.updateTeacher(teacher.getId(), teacher);

        //Then
        verify(teachersRepository, times(1)).updateTeacher(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), teacher.getEmail(), teacher.getUsername(), teacher.getOffice(), teacher.getTitle());
    }
}