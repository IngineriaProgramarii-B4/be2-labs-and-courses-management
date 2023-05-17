/*
package com.example.demo;

import com.example.demo.objects.Seminar;
import com.example.demo.repositories.SeminarRepository;
import com.example.demo.services.SeminarService;
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
public class SeminarServiceTest {
    @Mock
    private SeminarRepository seminarRepository;

    @InjectMocks
    private SeminarService seminarService;

    @BeforeEach
    public void setUp() {
        seminarService = new SeminarService(seminarRepository);
    }

    @Test
    public void testAddSeminar() {
        String name = "Math";

        seminarService.addSeminars(name);
        verify(seminarRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteSeminar() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Seminar seminar = new Seminar("Math", createdAt, updatedAt);

        doReturn(Optional.of(seminar)).when(seminarRepository).findById(null);

        seminarService.deleteSeminar(seminar);

        verify(seminarRepository, times(1)).save(any(Seminar.class));
        assertEquals(true, seminar.getisDeleted());
    }

    @Test
    public void testUpdateSeminar() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Seminar seminar = new Seminar("Math", createdAt, updatedAt);

        doReturn(Optional.of(seminar)).when(seminarRepository).findById(null);


        String newName = "Bio";

        seminarService.updateSeminar(seminar,newName);

        verify(seminarRepository, times(1)).save(any(Seminar.class));

        assertEquals(newName, seminar.getName());

        // Alte verificari pentru set-uri:
        UUID verificareSetId = UUID.randomUUID();

        Integer semesterVerificare =1;

        seminar.setId(verificareSetId);

        assertEquals(verificareSetId, seminar.getId());
    }

    @Test
    void testGetSeminarByName() {
        List<Seminar> lectures = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Seminar seminar1 = new Seminar("Math", createdAt, updatedAt);
        Seminar seminar2 = new Seminar( "Bio", createdAt, updatedAt);
        lectures.add(seminar1);
        lectures.add(seminar2);

        when(seminarRepository.findByName("Math")).thenReturn(lectures);

        List<Seminar> result = seminarService.getSeminarByName("Math");
        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Bio", result.get(1).getName());
    }


}*/
