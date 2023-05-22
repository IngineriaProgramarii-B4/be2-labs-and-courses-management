package com.example.user.service;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.security.services.TeachersService;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogTeachersServiceTest {
    @InjectMocks
    TeachersService teachersService;
    @Mock
    private TeachersRepository teachersRepository;

    private Teacher teacher;
    @BeforeEach
    public void setup() {
        teacher = new Teacher(
                UUID.randomUUID(),
                "Ciobaca",
                "Stefan",
                "stefan.ciobaca@uaic.com",
                "stefan.ciobaca",
                "C401",
                new HashSet<>(Arrays.asList(new Subject())),
                "Prof",
                "8e93c300-f251-11ed-a05b-0242ac120003"
        );
    }

    @Test
    void canGetAllTeachers() {
        given(teachersRepository.findTeachersByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)))
                .willReturn(List.of(teacher));
        // when
        Map<String, Object> args = new HashMap<>();
        List<Teacher> result = teachersService.getTeachersByParams(args);

        // then
        assertTrue(result.contains(teacher));
    }

    @Test
    void canGetTeacherById() {
        // given

        when(teachersRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        assertEquals(Optional.of(teacher), teachersRepository.findById(teacher.getId()));

        given(teachersRepository.findTeachersByParams(
                eq(teacher.getId()),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)
        ))
                .willReturn(List.of(teacher));

        Teacher get_result = teachersService.getTeachersByParams(Map.of("id", teacher.getId())).get(0);

        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        teachersRepository.save(teacherArgumentCaptor.capture());
        verify(teachersRepository).save(teacherArgumentCaptor.capture());

        Teacher captured = teacherArgumentCaptor.getValue();

        assertNotNull(get_result);
        assertEquals(get_result, teacher);

        // given invalid teacher id
        try {
            teachersService.getTeachersByParams(Map.of("id", UUID.randomUUID()));
        }
        catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    @Test
    void canAddTeacher() {

        when(teachersRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        assertEquals(Optional.of(teacher), teachersRepository.findById(teacher.getId()));
        teachersService.saveTeacher(teacher);

        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teachersRepository).save(teacherArgumentCaptor.capture());

        Teacher captured = teacherArgumentCaptor.getValue();

        assertEquals(captured, teacher);
    }

//    @Test
//    void canDeleteTeacher() {
//
//        when(teachersRepository.findById(teacher.getId().toString())).thenReturn(Optional.of(teacher));
//        assertEquals(Optional.of(teacher), teachersRepository.findById(teacher.getId().toString()));
//
//        teachersService.saveTeacher(teacher);
//        teachersService.delete(teacher);
//        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
//        verify(teacherRepository).save(teacherArgumentCaptor.capture());
//
//        Teacher captured = teacherArgumentCaptor.getValue();
//
//        assertEquals(captured, teacher);
//    }

}