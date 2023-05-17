package com.example.demo;

import com.example.demo.objects.Grade;
import com.example.demo.objects.Lecture;
import com.example.demo.objects.Seminar;
import com.example.demo.objects.Student;
import com.example.demo.repositories.GradeRepository;
import com.example.demo.services.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;
    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    public void setUp() {
        gradeService = new GradeService(gradeRepository);
    }

    @Test
    public void testAddGrade() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Student user = new Student("1234","John", "Doe", 2,"B4","john.doe@example.com", "password", createdAt, updatedAt);
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        double value = 7.5;
        double valFals = 12.3;
        assertThrows(IllegalArgumentException.class, () -> {
            Grade grade = new Grade(user.getId(),idLectures,idSeminars,valFals,date,createdAt,updatedAt);
        });

        int initialSize = gradeService.getGrades().size();
        gradeService.addGrade(user, idLectures, idSeminars, value);
        int newSize = gradeService.getGrades().size();

        assertEquals(initialSize + 1, newSize);
    }

    @Test
    public void testeSet(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Lecture lecture = new Lecture("PA", createdAt, updatedAt);
        Seminar seminar = new Seminar("PA", createdAt, updatedAt);
        Student user = new Student("1234","John", "Doe", 2,"B4","john.doe@example.com", "password", createdAt, updatedAt);
        double value = 7.5;

        Grade grade = new Grade();

        grade.setIdStudent(user.getId());
        grade.setIdLecture(lecture.getId());
        grade.setIdSeminar(seminar.getId());
        grade.setValue(value);
        grade.setUpdatedAt(updatedAt);
        grade.setDate(date);

        assertEquals(date, grade.getDate());
        assertEquals(user.getId(),grade.getIdStudent());
        assertEquals(lecture.getId(),grade.getIdLecture());
        assertEquals(seminar.getId(),grade.getIdSeminar());
        assertEquals(value, grade.getValue());
        assertEquals(updatedAt, grade.getUpdatedAt());
    }

    @Test
    public void testUpdateGrade(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new java.util.Date();
        Date createdAt= new java.util.Date(formatter.format(date));
        Date updatedAt= new java.util.Date(formatter.format(date));
        Student user = new Student("1234","John", "Doe", 2,"B4","john.doe@example.com", "password", createdAt, updatedAt);
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        double value = 7.5;
        Grade grade = new Grade(user.getId(),idLectures,idSeminars,value,date,createdAt,updatedAt);

        doReturn(Optional.of(grade)).when(gradeRepository).findById(null);

        double newValue= 10;

        gradeService.updateGrade(grade, newValue);

        verify(gradeRepository, times(1)).save(any(Grade.class));
        assertEquals(newValue, grade.getValue());

        //Niste teste pentru setValue
        double valMaiMare = 11;
        assertThrows(IllegalArgumentException.class, () -> {
            grade.setValue(valMaiMare);
        });

        double valMaiMica = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            grade.setValue(valMaiMica);
        });
    }

    @Test
    public void testDeleteGrade() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new java.util.Date();
        Date createdAt= new java.util.Date(formatter.format(date));
        Date updatedAt= new java.util.Date(formatter.format(date));
        Student user = new Student("1234","John", "Doe", 2,"B4","john.doe@example.com", "password", createdAt, updatedAt);
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        double value = 7.5;
        Grade grade = new Grade(user.getId(),idLectures,idSeminars,value,date,createdAt,updatedAt);

        doReturn(Optional.of(grade)).when(gradeRepository).findById(null);

        gradeService.deleteGrade(grade);

        verify(gradeRepository, times(1)).save(any(Grade.class));
        assertEquals(true, grade.getIsDeleted());
    }

    @Test
    public void testGetGradeByIdStudent(){
        List<Grade> gradeList=new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        UUID idStudent = UUID.randomUUID();
        double value = 7.5;
        Grade expectedGrade= new Grade(idStudent, idLectures,idSeminars, value,  date, createdAt,  updatedAt);
        gradeList.add(expectedGrade);
        when(gradeRepository.findByIdStudent(idStudent)).thenReturn(gradeList);
        List<Grade> result= gradeService.getGradeByIdStudent(idStudent);
        assertEquals(1, result.size());
        assertEquals(idStudent, result.get(0).getIdStudent());

    }
    @Test
    public void testGetGradeByIdLecture(){
        List<Grade> gradeList=new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        UUID idStudent = UUID.randomUUID();
        double value = 7.5;
        Grade expectedGrade= new Grade(idStudent, idLectures,idSeminars, value,  date, createdAt,  updatedAt);
        gradeList.add(expectedGrade);
        when(gradeRepository.findByIdLecture(idLectures)).thenReturn(gradeList);
        List<Grade> result= gradeService.getGradesForALecture(idLectures);
        assertEquals(1, result.size());
        assertEquals(idLectures, result.get(0).getIdLecture());
    }
    @Test
    public void testGetGradeByIdSeminar(){
        List<Grade> gradeList=new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        UUID idLectures = UUID.randomUUID();
        UUID idSeminars = UUID.randomUUID();
        UUID idStudent = UUID.randomUUID();
        double value = 7.5;
        Grade expectedGrade= new Grade(idStudent, idLectures,idSeminars, value,  date, createdAt,  updatedAt);
        gradeList.add(expectedGrade);
        when(gradeRepository.findByIdSeminar(idSeminars)).thenReturn(gradeList);
        List<Grade> result= gradeService.getGradesForASeminar(idSeminars);
        assertEquals(1, result.size());
        assertEquals(idSeminars, result.get(0).getIdSeminar());

    }

}