package com.example.catalog.repositories;

import com.example.catalog.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// <-------------------------------- FROM CATALOG ----------------------------------> //

@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {

    @Query("SELECT g FROM Grade g WHERE g.id= ?1")
    Optional<Grade> getGradeById(UUID id);

    @Modifying
    @Query("UPDATE Grade g set g.value= ?1 where g.id= ?2")
    int updateGrade(int value,UUID id);
}
