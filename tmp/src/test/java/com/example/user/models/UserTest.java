//package com.example.user.models;
//
//import com.example.security.objects.Student;
//import com.example.security.objects.Teacher;
//import com.example.security.objects.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.UUID;
//
//import static org.junit.Assert.assertNotEquals;
//
//class UserTest {
//
//    private Student student1, student2;
//
//    @BeforeEach
//    public void setup() {
//        student1 = new Student(
//                "testName",
//                "testSurename",
//                "testemail@mail.com",
//                "testUser",
//                1,
//                1,
//                "testRegistrationNumber",
//                null
//        );
//
//        student2 = new Student(
//                "testName",
//                "testSurename",
//                "testemail@mail.com",
//                "testUser",
//                1,
//                1,
//                "testRegistrationNumber",
//                null
//        );
//    }
//
//    @Test
//    void testEquals()
//    {
//        //
//        //Given
//        //
//        User user1 = new Student();
//
//        //
//        //When
//        //
//        User user2 = new Teacher();
//
//        //
//        //Then
//        //
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void testEqualsToNull()
//    {
//        //
//        //Given
//        //
//        User user1 = new Student();
//
//        //
//        //When
//        //
//        User user2 = null;
//
//        //
//        //Then
//        //
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void testEqualsToNullParams()
//    {
//        //
//        //Given
//        //
//        Student student = student1;
//
//        //
//        //When
//        //
//        student.setLastname("ceva");
//
//        //
//        //Then
//        //
//        assertNotEquals(student,student2);
//    }
//
//    @Test
//    void testEqualsToNullParams2()
//    {
//        //
//        //Given
//        //
//        Student student = student1;
//
//        //
//        //When
//        //
//        student.setFirstname("ceva");
//
//        //
//        //Then
//        //
//        assertNotEquals(student,student2);
//    }
//
//    @Test
//    void testEqualsToNullParams3()
//    {
//        //
//        //Given
//        //
//        Student student = student1;
//
//        //
//        //When
//        //
//        student.setUsername("ceva");
//
//        //
//        //Then
//        //
//        assertNotEquals(student,student2);
//    }
//
//    @Test
//    void testEqualsToNullParams4()
//    {
//        //
//        //Given
//        //
//        Student student = student1;
//
//        //
//        //When
//        //
//        student.setEmail("ceva");
//
//        //
//        //Then
//        //
//        assertNotEquals(student, student2);
//    }
//
//    @Test
//    void testEqualsToNullParams5()
//    {
//        //
//        //Given
//        //
//        Student student = student1;
//
//        //
//        //When
//        //
//        student.setId(UUID.randomUUID());
//
//        //
//        //Then
//        //
//        assertNotEquals(student, student2);
//    }
////
////    @Test
////    void testEqualsToNullParams6()
////    {
////        //
////        //Given
////        //
////        Student student = student1;
////
////        //
////        //When
////        //
////        student.setType(3);
////
////        //
////        //Then
////        //
////        assertNotEquals(student, student2);
////    }
//}
