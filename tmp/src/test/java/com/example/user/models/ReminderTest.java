package com.example.user.models;

import com.example.security.objects.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ReminderTest {

    private Reminder reminder1, reminder2, reminder3;
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
                reminder1.getId(),
                "25.04.2023 14:30",
                "Examen la pedagogie",
                "Examen maine la pedagogie la ora 16 in C2"
        );

        reminder3 = new Reminder(
                tempStudent,
                "25.04.2023 14:30",
                "Examen",
                "Examen la oop"
        );
    }

    @Test
    void testEqualsToNullParams() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setDueDateTime(LocalDateTime.parse("24.04.2023 14:30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams1() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //Then
        //
        assertEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams2() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setTitle("ceva");

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams3() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setId(UUID.randomUUID());

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams4() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setCreatorUsername("ceva");

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams5() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setCreatorId(UUID.randomUUID());

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testEqualsToNullParams6() {
        //
        //Given
        //
        Reminder reminder = reminder2;

        //
        //When
        //
        reminder.setDeleted(true);

        //
        //Then
        //
        assertNotEquals(reminder, reminder1);
    }

    @Test
    void testGetDeleted()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setDeleted(false);

        //
        //Then
        //
        assertFalse(reminder.getIsDeleted());

    }

    @Test
    void testSetDeleted()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setDeleted(false);

        //
        //Then
        //
        assertFalse(reminder.getIsDeleted());

    }

    @Test
    void testSetCreatorUsername()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setCreatorUsername("testUsername");

        //
        //Then
        //
        assertEquals("testUsername",reminder.getCreatorUsername());

    }

    @Test
    void testSetCreatorId()
    {
        //
        //Given
        //
        UUID id = new UUID(0x101abc, 0x99eff);
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setCreatorId(id);

        //
        //Then
        //
        assertEquals(reminder.getCreatorId(), id);

    }

    @Test
    void testSetId()
    {
        //
        //Given
        //
        UUID id = new UUID(0x101abc, 0x99eff);
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setId(id);

        //
        //Then
        //
        assertEquals(reminder.getId(), id);

    }

    @Test
    void testSetDueDateTime()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        LocalDateTime localDateTime = LocalDateTime.now();
        reminder.setDueDateTime(localDateTime);

        //
        //Then
        //
        assertEquals(reminder.getDueDateTime(), localDateTime);

    }

    @Test
    void testSetTitle()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setTitle("testTitle");

        //
        //Then
        //
        assertEquals("testTitle", reminder.getTitle());

    }


    @Test
    void testSetDescription()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        reminder.setDescription("test description");

        //
        //Then
        //
        assertEquals("test description",reminder.getDescription());

    }

    @Test
    void testEqualsToNull()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();

        //
        //When
        //
        Reminder nullReminder = null;


        //
        //Then
        //
        assertNotEquals(reminder, nullReminder);

    }

    @Test
    void testEqualsToSameClass()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();
        Student student = new Student();
        //
        //Then
        //
        assertNotEquals(reminder, student);

    }

    @Test
    void testNotEqual()
    {
        //
        //Given
        //
        Reminder reminder1 = new Reminder();
        Reminder reminder2 = new Reminder();
        //
        //When
        //
        reminder1.setTitle("testName");
        reminder1.setDescription("firstDescription");
        reminder2.setTitle("testName");
        reminder2.setDescription("secondDescription");



        //
        //Then
        //
        assertNotEquals(reminder1, reminder2);

    }

    @Test
    void testToString()
    {
        //
        //Given
        //
        Reminder reminder = new Reminder();
        //
        //When
        //
        reminder.setTitle("firstReminder");
        reminder.setDescription("this is an important reminder");
        reminder.setDeleted(false);
        //
        //Then
        //
        assertEquals("Reminder{creatorId=null, creatorUsername='null', id=null, dueDateTime=null, title='firstReminder', description='this is an important reminder'}",reminder.toString());
    }

    @Test
    void testHashCode()
    {
        //
        //Given
        //
        Reminder reminder1 = new Reminder();
        Reminder reminder2 = new Reminder();
        //
        //When
        //

        reminder1.setTitle("testTitle");
        reminder1.setDescription("test description");
        reminder2.setTitle("testTitle");
        reminder2.setDescription("test description");
        //
        //Then
        //
        assertEquals(reminder1.hashCode(), reminder2.hashCode());
    }





}
