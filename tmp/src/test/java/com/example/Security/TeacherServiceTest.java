/*
package com.example.demo;

import com.example.demo.objects.Teacher;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.services.TeacherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    public void testAddTeacher() {
        String registrationNumber = "A112";
        String firstName = "John";
        String lastName = "Doe";
        String degree = "PhD";
        String mail = "johndoe@example.com";
        String password = "password";

        teacherService.addTeacher(firstName, lastName, degree, mail, password);
        verify(teacherRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteTeacher() {
        String firstName = "John";
        String lastName = "Doe";
        String degree = "PhD";
        String mail = "johndoe@example.com";
        String password = "password";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Teacher teacher = new Teacher(firstName, lastName, degree, mail, password, createdAt, updatedAt);
        doReturn(Optional.of(teacher)).when(teacherRepository).findById(null);

        teacherService.deleteTeacher(teacher);

        verify(teacherRepository, times(1)).save(any(Teacher.class));
        assertEquals(true, teacher.getIsDeleted());
    }

    @Test
    public void testUpdateTeacher() {
        String firstName = "John";
        String lastName = "Doe";
        String degree = "PhD";
        String mail = "johndoe@example.com";
        String password = "password";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Teacher teacher = new Teacher(firstName, lastName, degree, mail, password, createdAt, updatedAt);

        doReturn(Optional.of(teacher)).when(teacherRepository).findById(null);


        String newFirstName = "Jane";
        String newLastName = "Smith";
        teacherService.updateTeacher(teacher,newFirstName, newLastName);

        verify(teacherRepository, times(1)).save(any(Teacher.class));

        assertEquals(newFirstName, teacher.getFirstName());
        assertEquals(newLastName, teacher.getLastName());

        UUID verificareSetId = UUID.randomUUID();
        String parolaVerificare ="parola";
        String mailVerificare = "diana@gmail.com";

        teacher.setId(verificareSetId);
        teacher.setPassword(parolaVerificare);
        teacher.setMail(mailVerificare);

        assertEquals(verificareSetId, teacher.getId());
        assertEquals(parolaVerificare, teacher.getPassword());
        assertEquals(mailVerificare, teacher.getMail());
    }
    @Test
    void testGetTeacherByLastName() {
        List<Teacher> teachers = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Teacher teacher1 = new Teacher("John", "Doe", "dr", "johndoe@example.com", "password", createdAt, updatedAt);
        Teacher teacher2 = new Teacher("Marie", "Lpc", "Dr", "mariedoe@example.com", "password", createdAt, updatedAt);
        teachers.add(teacher1);
        teachers.add(teacher2);

        when(teacherRepository.findByLastName("Doe")).thenReturn(teachers);

        List<Teacher> result = teacherService.getTeacherByLastName("Doe");
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Marie", result.get(1).getFirstName());
    }
    @Test
    void testGetTeacherByDegree() {
        List<Teacher> teachers = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Teacher teacher1 = new Teacher("John", "Doe", "Asistent", "johndoe@example.com", "password", createdAt, updatedAt);
        Teacher teacher2 = new Teacher("Marie", "Lpc", "Dr", "mariedoe@example.com", "password", createdAt, updatedAt);
        teachers.add(teacher1);
        teachers.add(teacher2);

        when(teacherRepository.findByDegree("Dr")).thenReturn(teachers);

        List<Teacher> result = teacherService.getTeacherByDegree("Dr");
        assertEquals(2, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Lpc", result.get(1).getLastName());
    }
    @Test
    public void testGetTeacherByMail() {
        // Given
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        String mail = "johndoe@example.com";
        Teacher expectedTeacher = new Teacher("John", "Doe", "Dr", "johndoe@example.com", "password", createdAt, updatedAt);

        when(teacherRepository.findByMail(mail)).thenReturn(expectedTeacher);

        // When
        Teacher retrievedTeacher = teacherService.getTeacherByMail(mail);

        // Then
        Assertions.assertNotNull(retrievedTeacher);
        assertEquals(expectedTeacher.getFirstName(), retrievedTeacher.getFirstName());
        assertEquals(expectedTeacher.getLastName(), retrievedTeacher.getLastName());
        assertEquals(expectedTeacher.getMail(), retrievedTeacher.getMail());
    }

}*/
