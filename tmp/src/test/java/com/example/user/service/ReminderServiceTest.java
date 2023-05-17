package com.example.user.service;

import com.example.security.objects.Student;
import com.example.user.models.Reminder;
import com.example.user.repository.RemindersRepository;
import com.example.user.services.RemindersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReminderServiceTest {
    @InjectMocks
    RemindersService remindersService;

    @Mock
    RemindersRepository remindersRepository;

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
        List<Reminder> expected = List.of(reminder1);

        Map<String, Object> args = new HashMap<>();

        args.put("creatorUsername", reminder1.getCreatorUsername());
        args.put("id", reminder1.getId());

        given(remindersRepository.findRemindersByParams(
                reminder1.getCreatorUsername(),
                reminder1.getId()
        )).willReturn(expected);

        //When
        List<Reminder> result = remindersService.getRemindersByParams(args);

        //Then
        assertTrue(result.containsAll(expected));
    }

    @Test
    void saveReminderTest() {
        //When
        when(remindersRepository.save(reminder1)).thenReturn(reminder1);

        remindersService.saveReminder(reminder1);

        //Then
        verify(remindersRepository, times(1)).save(reminder1);
    }

    @Test
    void removeReminderTest() {
        //When
        doNothing().when(remindersRepository).remove(reminder1.getId());

        remindersService.removeReminder(reminder1.getId());

        //Then
        verify(remindersRepository, times(1)).remove(reminder1.getId());
    }

    @Test
    void updateReminderTest() {
        //When
        doNothing().when(remindersRepository).updateReminder(reminder1.getId(), reminder1.getCreatorId(), reminder1.getCreatorUsername(), reminder1.getDueDateTime(), reminder1.getTitle(), reminder1.getDescription());

        remindersService.updateReminder(reminder1.getId(), reminder1);

        //Then
        verify(remindersRepository, times(1)).updateReminder(reminder1.getId(), reminder1.getCreatorId(), reminder1.getCreatorUsername(), reminder1.getDueDateTime(), reminder1.getTitle(), reminder1.getDescription());
    }
}
