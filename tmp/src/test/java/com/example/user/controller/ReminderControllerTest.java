package com.example.user.controller;

import com.example.security.objects.Student;
import com.example.user.controllers.RemindersController;
import com.example.user.models.Reminder;
import com.example.user.services.RemindersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(ReminderControllerTest.class)
@AutoConfigureMockMvc(addFilters = false)
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private RemindersController remindersController;

    @Mock
    private RemindersService remindersService;

    private Reminder reminder1, reminder2;
    private Student tempStudent;

    @BeforeEach
    public void setup() {
        tempStudent = new Student(
                UUID.randomUUID(),
                "Organized",
                "Student",
                "ilovereminders@yahoo.com",
                "reminderUser409",
                3,
                2,
                "123REM456",
                null
        );

        reminder1 = new Reminder(
                tempStudent,
                UUID.randomUUID(),
                "25.04.2023 14:30",
                "Examen la pedagogie",
                "Examen maine la pedagogie la ora 16 in C2"
        );

        reminder2 = new Reminder(
                tempStudent,
                UUID.randomUUID(),
                "06.06.2023 11:30",
                "Examen-IP",
                "Primul examen din sesiune"
        );
    }

    @Test
    void getRemindersByParamsTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("creatorId", reminder1.getCreatorId(), "id", reminder1.getId()))).thenReturn(List.of(reminder1));

        ResponseEntity<List<Reminder>> response = remindersController.getRemindersByParams(reminder1.getCreatorId(), reminder1.getId());

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRemindersByParamsNonExistentTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("creatorUsername", reminder1.getCreatorUsername(), "id", reminder1.getId()))).thenReturn(Collections.emptyList());

        ResponseEntity<List<Reminder>> response = remindersController.getRemindersByParams(reminder1.getCreatorId(), reminder1.getId());

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getRemindersOfLoggedUserTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("creatorId", reminder1.getCreatorId()))).thenReturn(List.of(reminder1, reminder2));

        ResponseEntity<List<Reminder>> response = remindersController.getRemindersOfLoggedUser(reminder1.getCreatorId());

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRemindersOfLoggedUserNonExistentTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("creatorUsername", reminder1.getCreatorUsername()))).thenReturn(Collections.emptyList());

        ResponseEntity<List<Reminder>> response = remindersController.getRemindersOfLoggedUser(reminder1.getCreatorId());

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateReminderTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        doNothing().when(remindersService).updateReminder(reminder1.getId(), reminder1);
        when(remindersService.getRemindersByParams(Map.of("id", reminder1.getId()))).thenReturn(List.of(reminder1));

        ResponseEntity<String> response = remindersController.updateReminder(reminder1.getId(), reminder1);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateReminderNonExistentTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("id", reminder1.getId()))).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = remindersController.updateReminder(reminder1.getId(), reminder1);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void saveReminderTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        doNothing().when(remindersService).saveReminder(reminder1);

        ResponseEntity<Reminder> response = remindersController.saveReminder(reminder1);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteReminderTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        doNothing().when(remindersService).removeReminder(reminder1.getId());
        when(remindersService.getRemindersByParams(Map.of("id", reminder1.getId()))).thenReturn(List.of(reminder1));

        ResponseEntity<Void> response = remindersController.deleteReminder(reminder1.getId());

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteReminderNonExistentTest() {
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //When
        when(remindersService.getRemindersByParams(Map.of("id", reminder1.getId()))).thenReturn(Collections.emptyList());

        ResponseEntity<Void> response = remindersController.deleteReminder(reminder1.getId());

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
