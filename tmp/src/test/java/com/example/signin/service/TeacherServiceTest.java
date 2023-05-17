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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacherService = new TeacherService(teacherRepository, passwordEncoder);
    }

    @Test
    void testAddTeacher() {
        Teacher teacher = new Teacher();
        teacher.setRegistrationNumber("123");

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        teacherService.addTeacher(teacher);

        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testUpdateTeacher() {
        String registrationNumber = "123";
        String newPassword = "newPassword";
        Teacher teacher = new Teacher();
        teacher.setRegistrationNumber(registrationNumber);
        teacher.setPassword("oldPassword");

        when(teacherRepository.findByRegistrationNumber(registrationNumber)).thenReturn(teacher);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        assertThrows(StudentNotFoundException.class, () -> teacherService.updateTeacher(registrationNumber, newPassword));

        verify(teacherRepository, times(1)).findByRegistrationNumber(registrationNumber);
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void testGetTeacherByRegistrationNumber() {
        String registrationNumber = "123";

        Teacher teacher = new Teacher();
        teacher.setRegistrationNumber(registrationNumber);

        when(teacherRepository.findByRegistrationNumber(registrationNumber)).thenReturn(teacher);

        Teacher found = teacherService.getTeacherByRegistrationNumber(registrationNumber);

        assertEquals(found, teacher);
        verify(teacherRepository, times(1)).findByRegistrationNumber(registrationNumber);
    }
    @Test
    void testUpdateTeacher_WhenTeacherNotFound() {
        String registrationNumber = "123";

        String newPassword = "newPassword";

        when(teacherRepository.findByRegistrationNumber(registrationNumber)).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> teacherService.updateTeacher(registrationNumber, newPassword));

        verify(teacherRepository, times(1)).findByRegistrationNumber(registrationNumber);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(teacherRepository);
    }

}