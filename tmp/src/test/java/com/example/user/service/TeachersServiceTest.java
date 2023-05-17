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
        List<Teacher> expected = List.of(teacher);

        Map<String, Object> args = new HashMap<>();

        args.put("office", "OfficeTest");

        args.put("id", "");

        given(teachersRepository.findTeachersByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                eq("OfficeTest"),
                nullable(String.class)))
                .willReturn(expected);

        //When
        List<Teacher> result = teachersService.getTeachersByParams(args);

        //Then
        assertTrue(result.containsAll(expected));
    }

    @Test
    void getTeachersByParamsTitleTest() {
        //Given
        List<Teacher> expected = List.of(teacher);

        Map<String, Object> args = new HashMap<>();

        args.put("title", "TitleTest");

        given(teachersRepository.findTeachersByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                eq("TitleTest")))
                .willReturn(expected);

        //When
        List<Teacher> result = teachersService.getTeachersByParams(args);

        //Then
        assertTrue(result.containsAll(expected));
    }

    @Test
    void getTeachersByParamsIdTest() {
        //Given
        List<Teacher> expected = List.of(teacher);

        Map<String, Object> args = new HashMap<>();

        UUID idTest = UUID.randomUUID();

        args.put("id", idTest);

        given(teachersRepository.findTeachersByParams(
                eq(idTest),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)))
                .willReturn(expected);

        //When
        List<Teacher> result = teachersService.getTeachersByParams(args);

        //Then
        assertTrue(result.containsAll(expected));
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