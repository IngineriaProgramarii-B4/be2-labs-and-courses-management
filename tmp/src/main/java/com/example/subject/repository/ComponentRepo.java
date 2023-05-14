package com.example.subject.repository;

import com.example.subject.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComponentRepo extends JpaRepository<Component, UUID> {
   @Query("SELECT c FROM Subject s JOIN s.componentList c WHERE s.title = ?1 AND s.isDeleted = FALSE AND c.isDeleted = FALSE")
   List<Component> findAllBySubjectTitle(String title);

   @Query("SELECT c FROM Subject s JOIN s.componentList c WHERE s.title = ?1 AND s.isDeleted = FALSE AND c.type = ?2 AND c.isDeleted = FALSE")
   Optional<Component> findBySubjectTitleAndType(String subjectTitle, String type);
}
