package com.example.subject.repository;

import com.example.subject.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EvaluationRepo extends JpaRepository<Evaluation, UUID> {
    @Query("SELECT e FROM Subject s JOIN s.evaluationList e WHERE s.title = ?1 AND s.isDeleted = FALSE AND e.isDeleted = FALSE")
    List<Evaluation> findAllBySubjectTitle(String title);

    @Query("SELECT e FROM Subject s JOIN s.evaluationList e WHERE s.title = ?1 AND s.isDeleted = FALSE AND e.component = ?2 AND e.isDeleted = FALSE")
    Optional<Evaluation> findBySubjectTitleAndComponent(String subjectTitle, String component);
}
