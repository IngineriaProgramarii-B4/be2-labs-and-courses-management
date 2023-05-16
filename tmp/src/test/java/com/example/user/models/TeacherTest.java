package com.example.user.models;

import com.example.security.objects.Teacher;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class TeacherTest {
    @Test
    void testToString() {
        //
        //Given
        //
        Teacher teacher = new Teacher();
        //
        // When
        //
        teacher.setFirstname("testFirstname");
        teacher.setLastname("testLastname");
        teacher.setTitle("testTitle");

        //
        //Then
        //
        assertEquals("Teacher{taughtSubjects=[], title='testTitle', office='null', id=null, firstname='testFirstname', lastname='testLastname', email='null', username='null', password='null', registrationNumber='null'}", teacher.toString());

    }

//    @Test
//    void testHashCode() {
//        //
//        //Given
//        //
//        Teacher teacher1 = new Teacher();
//        Teacher teacher2 = new Teacher();
//
//        //
//        // When
//        //
//        teacher1.setFirstname("testFirstname");
//        teacher1.setLastname("testLastname");
//        teacher2.setFirstname("testFirstname");
//        teacher2.setLastname("testLastname-other");
//        //
//        //Then
//        //
//        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
//
//    }
}
