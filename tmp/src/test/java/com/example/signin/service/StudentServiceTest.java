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
    private Student student;
    private final String REGISTRATIONNUMBER="123";
    private final String NEWPASSWORD="newPassword";

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentsRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        studentService = new StudentService(studentRepository, passwordEncoder);

        student = new Student();
        student.setRegistrationNumber(REGISTRATIONNUMBER);


    }

    @Test
    void testAddStudent() {
        // Act
        when(studentRepository.save(student)).thenReturn(student);

        studentService.addStudent(student);

        // Assert
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        student.setPassword("oldPassword");

        // Act
        when(studentRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(student);
        when(passwordEncoder.encode(NEWPASSWORD)).thenReturn("encodedNewPassword");

        // Assert
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(REGISTRATIONNUMBER, NEWPASSWORD));

        verify(studentRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
        verify(passwordEncoder, times(1)).encode(NEWPASSWORD);
    }

    @Test
    void testGetStudentByRegistrationNumber() {
        // Act
        when(studentRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(student);

        Student found = studentService.getStudentByRegistrationNumber(REGISTRATIONNUMBER);

        // Assert
        assertEquals(found, student);
        verify(studentRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
    }

    @Test
    void testUpdateStudent_WhenStudentNotFound() {
        // Act
        when(studentRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(null);

        // Assert
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(REGISTRATIONNUMBER, NEWPASSWORD));

        verify(studentRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(studentRepository);
    }
}
