package com.example.security.repositories;

import com.example.security.objects.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeachersRepository extends JpaRepository<Teacher, UUID> {

    @Query("select a from Teacher a where (cast(?1 as uuid) is null or a.id = ?1) and (?2 is null or a.firstname = ?2) and (?3 is null or a.lastname = ?3) and (?4 is null or a.email = ?4) and (?5 is null or a.username = ?5) and (?6 is null or a.office = ?6) and (?7 is null or a.title = ?7)")
    List<Teacher> findTeachersByParams(UUID id, String firstname, String lastname, String email, String username, String office, String title);

    @Modifying
    @Query("update Teacher t set t.firstname = COALESCE(?2, t.firstname), " +
            "t.lastname = COALESCE(?3, t.lastname), " +
            "t.email = COALESCE(?4, t.email), " +
            "t.username = COALESCE(?5, t.username), " +
            "t.office = COALESCE(?6, t.office), " +
            "t.title = COALESCE(?7, t.title) " +
            "where t.id = ?1")
    void updateTeacher(UUID uuid, String firstname, String lastname, String email, String username, String office, String title);


    List<Teacher> findByLastname(String lastName);
    List<Teacher> findByTitle(String degree);
    Teacher findByEmail(String email);

    Boolean existsByEmail(String email);
    Teacher findByRegistrationNumber(String id);
}