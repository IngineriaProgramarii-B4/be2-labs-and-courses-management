package com.example.signin.service;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.signin.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    private TeacherService teacherService;

    @Mock
    private TeachersRepository teacherRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Teacher teacher;
    private final String REGISTRATIONNUMBER="123";
    private final String NEWPASSWORD="newPassword";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacherService = new TeacherService(teacherRepository, passwordEncoder);

        teacher = new Teacher();
        teacher.setRegistrationNumber(REGISTRATIONNUMBER);
    }

    @Test
    void testAddTeacher() {
        // Act
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        teacherService.addTeacher(teacher);

        // Assert
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testUpdateTeacher() {
        // Arrange
        teacher.setPassword("oldPassword");

        // Act
        when(teacherRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(teacher);
        when(passwordEncoder.encode(NEWPASSWORD)).thenReturn("encodedNewPassword");

        // Assert
        assertThrows(StudentNotFoundException.class, () -> teacherService.updateTeacher(REGISTRATIONNUMBER, NEWPASSWORD));

        verify(teacherRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
        verify(passwordEncoder, times(1)).encode(NEWPASSWORD);
    }

    @Test
    void testGetTeacherByRegistrationNumber() {
        // Act
        when(teacherRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(teacher);

        Teacher found = teacherService.getTeacherByRegistrationNumber(REGISTRATIONNUMBER);

        // Assert
        assertEquals(found, teacher);
        verify(teacherRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
    }

    @Test
    void testUpdateTeacher_WhenTeacherNotFound() {

        // Act
        when(teacherRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(null);

        // Assert
        assertThrows(StudentNotFoundException.class, () -> teacherService.updateTeacher(REGISTRATIONNUMBER, NEWPASSWORD));

        verify(teacherRepository, times(1)).findByRegistrationNumber(REGISTRATIONNUMBER);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(teacherRepository);
    }
}
