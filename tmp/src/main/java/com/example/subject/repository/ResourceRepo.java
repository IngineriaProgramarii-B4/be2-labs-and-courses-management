
package com.example.subject.repository;

import com.example.subject.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepo extends JpaRepository<Resource, UUID> {
    @Query("SELECT r FROM Subject s JOIN s.componentList c JOIN c.resources r WHERE s.title = ?1 AND s.isDeleted = FALSE AND c.type = ?2 AND c.isDeleted = FALSE AND r.isDeleted = FALSE")
    List<Resource> findAllBySubjectTitleAndComponentType(String title, String type);

    @Query("SELECT r FROM Subject s JOIN s.componentList c JOIN c.resources r WHERE s.title = ?1 AND s.isDeleted = FALSE AND c.type = ?2 AND c.isDeleted = FALSE AND r.title = ?3 AND r.isDeleted = FALSE")
    Optional<Resource> findBySubjectTitleAndComponentTypeAndResourceTitle(String subjectTitle, String componentType, String resourceTitle);
}

