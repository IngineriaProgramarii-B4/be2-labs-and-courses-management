package com.example.security.services;
import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.security.services.StudentsService;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentsRepository studentsRepository;

    private StudentsService studentsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentsService = new StudentsService(studentsRepository);
    }

    @Test
    public void testDeleteStudent() {
        // Create a sample student
        Student student = new Student();

        // Create some sample grades for the student
        List<Grade> grades = new ArrayList<>();
        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        grades.add(grade1);
        grades.add(grade2);
        student.addGrade(grade1);
        student.addGrade(grade2);

        // Mock the behavior of the repository
        when(studentsRepository.findStudentById(student.getId())).thenReturn(student);

        // Call the deleteStudent method
        Student deletedStudent = studentsService.deleteStudent(student.getId());

        // Verify that the student and grades are marked as deleted
        assertTrue(deletedStudent.getIsDeleted());
        for (Grade grade : grades) {
            assertTrue(grade.getIsDeleted());
        }

        // Verify that the repository method was called
        verify(studentsRepository, times(1)).findStudentById(student.getId());
    }

    @Test
    public void testGetStudentByEnrolledCourse() {
        // Create some sample students and courses
        Student student1 = new Student();
        Student student2 = new Student();
        Student student3 = new Student();

        Subject course1 = new Subject();
        course1.setTitle("Math");
        Subject course2 = new Subject();
        course2.setTitle("Physics");

        List<Student> allStudents = new ArrayList<>();
        allStudents.add(student1);
        allStudents.add(student2);
        allStudents.add(student3);

        student1.getEnrolledCourses().add(course1);
        student2.getEnrolledCourses().add(course2);
        student3.getEnrolledCourses().add(course1);
        student3.getEnrolledCourses().add(course2);

        // Mock the behavior of the repository
        when(studentsRepository.findStudentsByParams(null, null, null, null, null, 0, 0, null)).thenReturn(allStudents);

        // Call the getStudentByEnrolledCourse method
        Set<Student> searchedStudents = studentsService.getStudentByEnrolledCourse("Math");

        // Verify that the correct students are returned
        assertTrue(searchedStudents.contains(student1));
        assertTrue(searchedStudents.contains(student3));

        // Verify that the repository method was called
        verify(studentsRepository, times(1)).findStudentsByParams(null, null, null, null, null, 0, 0, null);
    }
   /* @Test
    public void testUpdateGrade() {
        // Create a sample student
        UUID studentId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);

        // Create a sample grade
        UUID gradeId = UUID.randomUUID();
        Grade grade = new Grade();
        grade.setId(gradeId);
        grade.setValue(8);
        grade.setEvaluationDate("01.01.2022");

        student.addGrade(grade);

        // Mock the behavior of the repository
        when(studentsRepository.findStudentById(student.getId())).thenReturn(student);

        // Call the updateGrade method
        Grade updatedGrade = studentsService.updateGrade(student.getId(), 9, "02.02.2022", grade.getId());

        // Verify that the grade is updated correctly
        assertEquals(9, updatedGrade.getValue());
        assertEquals("02.02.2022", updatedGrade.getEvaluationDate());

        // Verify that the repository method was called
        verify(studentsRepository, times(1)).findStudentById(student.getId());
    }*/
}
