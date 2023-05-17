/*
package com.example.demo;

import com.example.demo.objects.Lecture;
import com.example.demo.repositories.LectureRepository;
import com.example.demo.services.LectureService;

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
public class LectureServiceTest {
    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    public void setUp() {
        lectureService = new LectureService(lectureRepository);
    }

    @Test
    public void testAddLecture() {
        String name = "Math";

        lectureService.addLectures(name);
        verify(lectureRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateLecture() {
        String name ="Mate";

        Lecture lecture = new Lecture(name,null, null);

        doReturn(Optional.of(lecture)).when(lectureRepository).findById(null);

        String newName="Istorie";
        lectureService.updateLecture(lecture, newName);

        verify(lectureRepository, times(1)).save(any(Lecture.class));

        assertEquals(newName, lecture.getName());

        UUID verificareSetId = UUID.randomUUID();

        lecture.setId(verificareSetId);

        assertEquals(verificareSetId, lecture.getId());
    }

    @Test
    public void testDeleteLecture() {
        String name ="Mate";

        Lecture lecture = new Lecture(name,null, null);

        doReturn(Optional.of(lecture)).when(lectureRepository).findById(null);

        lectureService.deleteLecture(lecture);

        verify(lectureRepository, times(1)).save(any(Lecture.class));

        assertEquals(true, lecture.getIsDeleted());
    }

    @Test
    void testGetLectureByName() {
        List<Lecture> lectures = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Lecture lecture1 = new Lecture("Math", createdAt, updatedAt);
        Lecture lecture2 = new Lecture("Bio", createdAt, updatedAt);
        lectures.add(lecture1);
        lectures.add(lecture2);

        when(lectureRepository.findByName("Math")).thenReturn(lectures);

        List<Lecture> result = lectureService.getLectureByName("Math");
        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Bio", result.get(1).getName());
    }


}*/
