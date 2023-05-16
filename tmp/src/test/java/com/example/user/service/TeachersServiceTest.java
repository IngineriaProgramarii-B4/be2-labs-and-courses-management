package com.example.user.service;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.security.services.TeachersService;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
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
                UUID.randomUUID(),
                "FirstnameTest",
                "LastnameTest",
                "EmailTest@gmail.com",
                "UsernameTest",
                "OfficeTest",
                null,
                "TitleTest",
                "8e93c300-f251-11ed-a05b-0242ac120003"
        );
    }

    @AfterClass
    public void clean() {
        teachersRepository.delete(teacher);
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