package com.example.signin.service;

import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.signin.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private StudentsRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentsRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        studentService = new StudentService(studentRepository, passwordEncoder);
    }

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setRegistrationNumber("123");

        when(studentRepository.save(student)).thenReturn(student);

        studentService.addStudent(student);

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testUpdateStudent() {
        String registrationNumber = "123";
        String newPassword = "newPassword";
        Student student = new Student();
        student.setRegistrationNumber(registrationNumber);
        student.setPassword("oldPassword");

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(registrationNumber, newPassword));

        verify(studentRepository, times(1)).findByRegistrationNumber(registrationNumber);
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void testGetStudentByRegistrationNumber() {
        String registrationNumber = "123";
        Student student = new Student();
        student.setRegistrationNumber(registrationNumber);

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);

        Student found = studentService.getStudentByRegistrationNumber(registrationNumber);

        assertEquals(found, student);
        verify(studentRepository, times(1)).findByRegistrationNumber(registrationNumber);
    }
    @Test
    void testUpdateStudent_WhenStudentNotFound() {
        String registrationNumber = "123";

        String newPassword = "newPassword";

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(registrationNumber, newPassword));

        verify(studentRepository, times(1)).findByRegistrationNumber(registrationNumber);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(studentRepository);
    }

}
