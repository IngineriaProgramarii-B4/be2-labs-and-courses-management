package com.example.security.services;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeachersServiceTest {

    @Mock
    private TeachersRepository mockTeachersRepository;

    private TeachersService teachersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teachersService = new TeachersService(mockTeachersRepository);
    }

    @Test
    public void testGetTeachersByParams() {
        // Mock input parameters
        UUID id = UUID.randomUUID();
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String username = "johndoe";
        String office = "A123";
        String title = "Math Teacher";

        // Create a map with the input parameters
        Map<String, Object> params = new HashMap<>();
        params.put("id", id.toString());
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("email", email);
        params.put("username", username);
        params.put("office", office);
        params.put("title", title);

        // Mock the repository's response
        List<Teacher> expectedTeachers = Collections.singletonList(new Teacher());
        when(mockTeachersRepository.findTeachersByParams(id, firstname, lastname, email, username, office, title))
                .thenReturn(expectedTeachers);

        // Call the service method
        List<Teacher> actualTeachers = teachersService.getTeachersByParams(params);

        // Verify the result
        assertEquals(expectedTeachers, actualTeachers);
    }
    @Test
    public void testSaveTeacher() {
        // Create a sample teacher
        Teacher teacher = new Teacher();
        // Call the service method
        teachersService.saveTeacher(teacher);
        // Verify that the repository's save method was called with the teacher
        verify(mockTeachersRepository).save(teacher);
    }
    @Test
    public void testDeleteTeacher() {
        // Create a sample teacher with a specific UUID
        UUID teacherId = UUID.randomUUID();
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        // Mock the repository's response when finding the teacher by ID
        when(mockTeachersRepository.findTeacherById(teacherId)).thenReturn(teacher);

        // Call the service method
        Teacher deletedTeacher = teachersService.deleteTeacher(teacherId);

        // Verify that the repository's findTeacherById method was called with the teacherId
        verify(mockTeachersRepository).findTeacherById(teacherId);

        // Verify that the teacher object is marked as deleted
        assertTrue(deletedTeacher.getIsDeleted());
    }

    @Test
    public void testDeleteTeacher_NonexistentTeacher() {
        // Create a non-existent teacher ID
        UUID nonExistentTeacherId = UUID.randomUUID();

        // Mock the repository's response when finding the teacher by ID (returns null)
        when(mockTeachersRepository.findTeacherById(nonExistentTeacherId)).thenReturn(null);

        // Call the service method
        Teacher deletedTeacher = teachersService.deleteTeacher(nonExistentTeacherId);

        // Verify that the repository's findTeacherById method was called with the nonExistentTeacherId
        verify(mockTeachersRepository).findTeacherById(nonExistentTeacherId);

        // Verify that the deletedTeacher is null
        assertNull(deletedTeacher);
    }
    @Test
    public void testUpdateTeacher() {
        // Create a sample teacher with a specific UUID
        UUID teacherId = UUID.randomUUID();
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstname("John");
        teacher.setLastname("Doe");
        teacher.setEmail("john.doe@example.com");
        teacher.setUsername("johndoe");
        teacher.setOffice("A123");
        teacher.setTitle("Math Teacher");

        // Call the service method
        teachersService.updateTeacher(teacherId, teacher);

        // Verify that the repository's updateTeacher method was called with the correct parameters
        verify(mockTeachersRepository).updateTeacher(
                teacherId,
                teacher.getFirstname(),
                teacher.getLastname(),
                teacher.getEmail(),
                teacher.getUsername(),
                teacher.getOffice(),
                teacher.getTitle()
        );
    }
}
