package com.example.user.models;

import com.example.security.objects.Student;
import com.example.subject.model.Subject;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


class StudentTest {
    @Test
    void testToString() {
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
        assertEquals("Student{enrolledCourses=[], year=0, semester=0, grupa='null', maxGradeId=0, grades=[]}", student.toString());

    }

    @Test
    void testAddEnrolledCourse() {
        //
        //Given
        //
        Student student = new Student();
        Set<Subject> courses = new HashSet<>();
        courses.add(new Subject());
        //
        //When
        //
        student.addEnrolledCourse(new Subject());
        //
        //Then
        //
        assertEquals(student.getEnrolledCourses(), courses);

    }

    @Test
    void testHashCode() {
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
