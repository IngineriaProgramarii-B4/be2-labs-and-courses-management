package com.example.subject.repository;

import com.example.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectRepo extends JpaRepository<Subject, UUID> {

    @Query("SELECT s FROM Subject s WHERE s.title = ?1 AND s.isDeleted = FALSE")
    Optional<Subject> findSubjectByTitle(String title);

    @Query("SELECT s FROM Subject s WHERE s.year = ?1 AND s.semester = ?2 AND s.isDeleted = FALSE")
    List<Subject> findAllByYearAndSemester(int year, int semester);

    @NonNull
    @Query("SELECT s from Subject s WHERE s.isDeleted = FALSE")
    List<Subject> findAll();
}