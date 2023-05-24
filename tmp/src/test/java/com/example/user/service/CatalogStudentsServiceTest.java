package com.example.user.service;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.security.services.StudentsService;
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

import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogStudentsServiceTest {
    @InjectMocks
    StudentsService studentsService;
    @Mock
    StudentsRepository studentsRepository;

    private String subject;
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
                new HashSet<>(List.of(new Subject())));
        subject = "IP";
        grade = new Grade(7, subject, "12.12.2012");


        studentsService.saveStudent(student);

    }
    @Test
    void canGetAllStudents() {
        // given
        given(studentsRepository.findStudentsByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                anyInt(),
                anyInt(),
                nullable(String.class)))
                .willReturn(List.of(student));
        // when
        Map<String, Object> args = new HashMap<>();
        List<Student> result = studentsService.getStudentsByParams(args);

        // then
        assertTrue(result.contains(student));
    }

    @Test
    public void givenValidStudentId_whenGetStudentById_thenReturnsWantedStudent() {
        // given
        given(studentsService.getStudentById(student.getId())).willReturn(student);

        // when
        Optional<Student> get = Optional.ofNullable(studentsService.getStudentById(student.getId()));

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Optional<Student> captured = Optional.of(studentArgumentCaptor.getValue());

        assertNotNull(get);
        assertEquals(get, captured);
    }

    @Test
    public void givenInvalidStudentId_whenGetStudentById_thenReturnsNull(){
        // given not existing student
        assertNull(studentsService.getStudentById(UUID.randomUUID()));
    }
    @Test
    void canSaveStudent() {
        // given
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentsRepository).save(studentArgumentCaptor.capture());

        Student captured = studentArgumentCaptor.getValue();
        assertEquals(captured, student);

        // given null student
        Student shouldBeNull = studentsService.saveStudent(null);
        assertNull(shouldBeNull);
    }
    @Test
    public void givenValidStudentId_whenSaveStudent_thenReturnsSameStudent() {
        // given
        given(studentsService.getStudentById(student.getId())).willReturn(student);
        // when
        Student deleted = studentsService.deleteStudent(student.getId());
        // then
        assertTrue(deleted.getIsDeleted());
    }

    @Test
    public void givenInvalidStudentId_whenSaveStudent_thenReturnsNull() {
        // given
        Student mocked = new Student(UUID.randomUUID(), null, null, null, null, 0, 0, null, null);
        // when
        Student shouldBeNull = studentsService.deleteStudent(mocked.getId());
        // then
        assertNull(shouldBeNull);
    }

    @Test
    public void givenGrade_whenAddGrade_thenReturnsSameGrade() {

        // given
        given(studentsService.getStudentById(student.getId())).willReturn(student);

        // when
        Grade added = studentsService.addGrade(studentsService.getStudentById(student.getId()).getId(), grade);

        // then
        assertEquals(added, grade);
    }

    @Test
    public void givenValidGradeId_whenDeleteGrade_thenGradeIsDeleted() {

        // given
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);

        studentsService.addGrade(student.getId(), grade);

        // when
        studentsService.deleteGrade(studentsService.getStudentById(student.getId()).getId(), grade.getId());
        // then
        assertTrue(grade.getIsDeleted());
    }
    @Test
    public void givenInvalidGradeId_whenDeleteGrade_thenThrowNullException() {

        // given not existing grade
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        try {
            studentsService.deleteGrade(studentsService.getStudentById(student.getId()).getId(), UUID.randomUUID());
        }
        // then
        catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    @Test
    public void givenValidGradeId_whenGetGradeById_thenReturnsSameGrade() {

        // given
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        studentsService.addGrade(student.getId(), grade);

        // when
        Grade returned = studentsService.getGradeById(studentsService.getStudentById(student.getId()).getId(), grade.getId());

        // then
        assertEquals(returned, grade);

    }
    @Test
    public void givenInvalidGradeId_whenGetGradeById_thenThrowIllegalStateException(){
        // given not existing grade
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        try {
            // when
            studentsService.getGradeById(studentsService.getStudentById(student.getId()).getId(), UUID.randomUUID());
        }
        // then
        catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    @Test

    public void givenValidGradeId_whenUpdateGrade_thenReturnUpdatedGrade() {
        // given
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        studentsService.addGrade(student.getId(), grade);

        // when
        Grade updated = studentsService.updateGrade(studentsService.getStudentById(student.getId()).getId(), 6, null, grade.getId());
        // then
        assertEquals(updated, grade);
    }
    @Test
    public void givenInvalidGradeValue_whenUpdateGrade_thenThrowIllegalStateException() {
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        try {
            // given invalid grade
            studentsService.updateGrade(studentsService.getStudentById(student.getId()).getId(), 0, null, grade.getId());
        }
        // then
        catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    @Test
    public void givenInvalidGradeDate_whenUpdateGrade_thenReturnDefaultDateValue() {
        // given
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        String date = "12.12.2012";
        studentsService.addGrade(student.getId(), grade);
        // when
        studentsService.updateGrade(studentsService.getStudentById(student.getId()).getId(), 3, null, grade.getId());

        // then should be changed to default value
        assertEquals(date, grade.getEvaluationDate());
    }

    @Test
    void canUpdateGradeTest() {
        // given
        given(studentsService.getStudentById(student.getId()))
                .willReturn(student);
        studentsService.addGrade(student.getId(), grade);

        // when updating with non-existing student ID
        Grade shouldBeNull = studentsService.updateGrade(UUID.randomUUID(), 6, null, grade.getId());
        assertNull(shouldBeNull);

        // when updating with null value
        Grade updatedNullValue = studentsService.updateGrade(student.getId(), null, null, grade.getId());
        assertEquals(grade, updatedNullValue);

        UUID idForGrade = grade.getId();
        UUID idForStudent = student.getId();

        // when updating with invalid value
        assertThrows(IllegalStateException.class,
                () -> studentsService.updateGrade(idForStudent, 0, null, idForGrade));

        // when updating with value outside of range
        assertThrows(IllegalStateException.class,
                () -> studentsService.updateGrade(idForStudent, 11, null, idForGrade));

        // when updating with a valid value
        Grade updatedValue = studentsService.updateGrade(student.getId(), 8, null, grade.getId());
        assertEquals(8, updatedValue.getValue());

        // when updating with invalid evaluation date
        assertThrows(IllegalStateException.class,
                () ->  studentsService.updateGrade(student.getId(), 8, "invalid date", grade.getId()));

        // when updating with valid evaluation date
        Grade updatedValidDate = studentsService.updateGrade(student.getId(), 8, "10.05.2023", grade.getId());
        assertEquals("10.05.2023", updatedValidDate.getEvaluationDate());

        // when updating with the same evaluation date
        Grade updatedSameDate = studentsService.updateGrade(student.getId(), 8, "05.11.2022", grade.getId());
        assertEquals("05.11.2022", updatedSameDate.getEvaluationDate());

        // when updating with null value and different evaluation date
        Grade updatedNullValueAndDate = studentsService.updateGrade(student.getId(), null, "01.01.2022", grade.getId());
        assertEquals("01.01.2022", updatedNullValueAndDate.getEvaluationDate());
        assertEquals(grade, updatedNullValueAndDate);

        // when updating with the same evaluation date

        updatedSameDate = studentsService.updateGrade(student.getId(), 8, grade.getEvaluationDate(), grade.getId());
        assertEquals(grade.getEvaluationDate(), updatedSameDate.getEvaluationDate());
    }

    @Test
    void testToString() {
        Grade gradeToString = new Grade(7, subject, "12.12.2012");

        // expected output
        String expectedOutput = gradeToString.toString();

        // actual output
        String actualOutput = grade.toString();

        // compare the actual output with the expected output
        assertEquals(expectedOutput, actualOutput);
    }

}