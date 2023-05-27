package com.example.security.objects;
import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.subject.model.Subject;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void testAddGrade_AddsGradeToGradesList() {
        Student student = new Student();
        Grade grade = new Grade();
        grade.setId(UUID.randomUUID());

        student.addGrade(grade);

        assertTrue(student.getGrades().contains(grade));
    }

    @Test
    public void testGetGrades_ReturnsNonDeletedGrades() {
        Student student = new Student();
        Grade grade1 = new Grade();
        grade1.setId(UUID.randomUUID());
        Grade grade2 = new Grade();
        grade2.setId(UUID.randomUUID());
        Grade grade3 = new Grade();
        grade3.setId(UUID.randomUUID());
        grade3.setDeleted(true);

        student.addGrade(grade1);
        student.addGrade(grade2);
        student.addGrade(grade3);

        List<Grade> grades = student.getGrades();

        assertEquals(2, grades.size());
        assertTrue(grades.contains(grade1));
        assertTrue(grades.contains(grade2));
        assertFalse(grades.contains(grade3));
    }

    @Test
    public void testGetGradeById_ReturnsGradeWithMatchingId() {
        Student student = new Student();
        Grade grade1 = new Grade();
        grade1.setSubject("Mate");

        Grade grade2 = new Grade();


        student.addGrade(grade1);
        student.addGrade(grade2);

        Grade foundGrade = student.getGradeById(grade1.getId());

        assertEquals(grade1, foundGrade);
    }

    @Test
    public void testGetGradeById_ReturnsNullIfNoMatchingId() {
        Student student = new Student();
        Grade grade1 = new Grade();
        grade1.setId(UUID.randomUUID());
        Grade grade2 = new Grade();
        grade2.setId(UUID.randomUUID());

        student.addGrade(grade1);
        student.addGrade(grade2);

        Grade foundGrade = student.getGradeById(UUID.randomUUID());

        assertNull(foundGrade);
    }

    @Test
    public void testGetGradesBySubject_ReturnsGradesWithMatchingSubject() {
        Student student = new Student();

        Grade grade1 = new Grade();
        grade1.setSubject("Math");

        Grade grade2 = new Grade();
        grade2.setSubject("English");

        Grade grade3 = new Grade();
        grade3.setSubject("Math");

        student.addGrade(grade1);
        student.addGrade(grade2);
        student.addGrade(grade3);

        List<Grade> mathGrades = student.getGradesBySubject("Math");

        assertEquals(2, mathGrades.size());
        assertTrue(mathGrades.contains(grade1));
        assertTrue(mathGrades.contains(grade3));
        for(Grade it : mathGrades) {
            assertNotEquals(it.getSubject(), grade2.getSubject());
        }
    }
}
