package com.example.user.models;

import com.example.security.objects.Student;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

class StudentTest {

    @Test
    void testToString()
    {
        //
        //Given
        //
        Student student = new Student();

        //
        //When
        //
        student.setFirstname("testFirstname");
        student.setLastname("testLastname");

        //
        //Then
        //
        assertEquals("Student{enrolledCourses=[], year=0, semester=0, grupa='null', grades=[], id=null, firstname='testFirstname', lastname='testLastname', email='null', username='null', password='null', registrationNumber='null'}", student.toString());

    }

    @Test
    void testAddEnrolledCourse() {
        //
        //Given
        //
        Student student = new Student();
        Subject course = new Subject("SO", 5, 2, 2, "materia SO", List.of(), List.of(), false);

        //
        //When
        //
        student.addEnrolledCourse(course);

        //
        //Then
        //
        assertEquals(student.getEnrolledCourses(), Set.of(course));

    }

    @Test
    void testHashCode()
    {
        //
        //Given
        //
        Student student1 = new Student();
        Student student2 = new Student();
        //
        //When
        //
        student1.setFirstname("testFirstname");
        student1.setLastname("testLastname");
        student2.setFirstname("testFirstname");
        student2.setLastname("testLastname");
        //
        //Then
        //
        assertEquals(student1.hashCode(), student2.hashCode());

    }
}
