package com.example.user.service;

import com.example.catalog.models.Grade;
import com.example.catalog.repositories.GradeRepository;
import com.example.catalog.services.GradeService;
import com.example.security.objects.Student;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GradeServiceTest {
    @InjectMocks
    GradeService gradeService;
    @Mock
    GradeRepository gradeRepository;
    private Student student;
    private Grade grade;

    @BeforeEach
    public void setup() {
        student = new Student(
                UUID.randomUUID(),
                "Florin",
                "Rotaru",
                "florin.eugen@uaic.ro",
                "florin02",
                2,
                4,
                "123FAKE92929",
                new HashSet<>(Arrays.asList(new Subject())));

        String subject = "IP";
        grade = new Grade(7, subject, "12.02.1996");

    }

    @Test
    void givenValidGradeId_whenGetGradeById_thenReturnsGrade() {
        when(gradeRepository.getGradeById(grade.getId())).thenReturn(Optional.of(grade));
        assertEquals(Optional.of(grade), gradeRepository.getGradeById((grade.getId())));
        // given existing grade
        Optional<Grade> get = Optional.ofNullable(gradeService.getGradeById(grade.getId()));

        //then

        ArgumentCaptor<Grade> gradeArgumentCaptor = ArgumentCaptor.forClass(Grade.class);

        gradeRepository.save(get.get());

        verify(gradeRepository).save(gradeArgumentCaptor.capture());

        Optional<Grade> captured = Optional.ofNullable(gradeArgumentCaptor.getValue());

        assertNotNull(get);
        assertEquals(get, captured);

        // given not existing grade
        assertNull(gradeService.getGradeById(UUID.randomUUID()));
    }

    @Test
    void canGetGradesTest() {
        // when
        gradeService.getGrades();

        // then
        verify(gradeRepository).findAll();

    }
}