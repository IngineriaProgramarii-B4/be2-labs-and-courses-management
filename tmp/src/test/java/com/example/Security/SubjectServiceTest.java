/*
package com.example.demo;


import com.example.demo.objects.Subject;
import com.example.demo.repositories.SubjectRepository;
import com.example.demo.services.SubjectService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    public void setup(){
        subjectService = new SubjectService(subjectRepository);
    }

    @Test
    public void testAddSubject(){
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        subjectService.addSubject(name, an, semestru, credite,idL,idS);
        verify(subjectRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteSubject() {
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Subject subject = new Subject(name, an, semestru, credite,idL,idS, createdAt, updatedAt);
        doReturn(Optional.of(subject)).when(subjectRepository).findById(null);

        subjectService.deleteSubject(subject);

        verify(subjectRepository, times(1)).save(any(Subject.class));
        assertEquals(true, subject.getisDeleted());
    }
    @Test
    public void testUpdateSubject() {
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Subject subject = new Subject(name, an, semestru, credite,idL,idS, createdAt, updatedAt);

        doReturn(Optional.of(subject)).when(subjectRepository).findById(null);

        String newName = "Math";
        int newCredits = 3;
        subjectService.updateSubject(subject,newName, newCredits);

        verify(subjectRepository, times(1)).save(any(Subject.class));

        assertEquals(newName, subject.getName());
        assertEquals(newCredits, subject.getCredits());

        // Alte verificari pentru set-uri:
        UUID verificareSetId = UUID.randomUUID();
        Integer anVerificare = 3;
        Integer semesterVerificare =1;
        Integer idLVerificare = 1;
        Integer idSVerificare = 1;

        subject.setYear(anVerificare);
        subject.setId(verificareSetId);
        subject.setSemester(1);
        subject.setIdLectures(idLVerificare);
        subject.setIdSeminars(idSVerificare);

        assertEquals(verificareSetId, subject.getId());
        assertEquals(semesterVerificare, subject.getSemester());
        assertEquals(anVerificare, subject.getYear());
        assertEquals(idLVerificare,subject.getIdLectures());
        assertEquals(idSVerificare, subject.getIdSeminars());
    }

    @Test
    public void testGetSubjectByName(){
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));

        Subject subject = new Subject(name, an, semestru, credite,idL,idS, createdAt, updatedAt);
        when(subjectRepository.findByName(name)).thenReturn(subject);

        Subject result = subjectService.getSubjectByName(name);

        assertEquals(subject,result);
    }

    @Test
    public void testGetSubjectByYear(){
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        List<Subject> subjects = new ArrayList<>();

        Subject subject = new Subject(name, an, semestru, credite,idL,idS, createdAt, updatedAt);
        subjects.add(subject);

        when(subjectRepository.findByYear(2)).thenReturn(subjects);

        List<Subject> result = subjectService.getSubjectByYear(an);

        assertEquals(1, subjects.size());
        assertEquals(subjects,result);
    }

    @Test
    public void testGetSubjectByYearAndSemester(){
        String name = "materie";
        Integer an = 2;
        Integer semestru = 2;
        Integer credite = 5;
        Integer idL = 1;
        Integer idS = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        List<Subject> subjects = new ArrayList<>();

        Subject subject = new Subject(name, an, semestru, credite,idL,idS, createdAt, updatedAt);
        subjects.add(subject);

        when(subjectRepository.findByYearAndSemester(2,2)).thenReturn(subjects);

        List<Subject> result = subjectService.getSubjectByYearAndSemester(an, semestru);

        assertEquals(1, subjects.size());
        assertEquals(subjects,result);
    }
}
*/
