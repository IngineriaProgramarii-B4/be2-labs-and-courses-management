package com.example.security.repositories;

import com.example.security.objects.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentsRepository extends JpaRepository<Student, UUID> {
    @Query("select a from Student a where (cast(?1 as uuid)  is null or a.id = ?1) and (?2 is null or a.firstname = ?2) and (?3 is null or a.lastname = ?3) and (?4 is null or a.email = ?4) and (?5 is null or a.username = ?5) and (?6 = 0 or a.year = ?6) and (?7 = 0 or a.semester = ?7) and (?8 is null or a.registrationNumber = ?8)")
    List<Student> findStudentsByParams(UUID id, String firstname, String lastname, String email, String username, Integer year, Integer semester, Long registrationNumber);
    // in contextul asta poti lasa asa sau sa stergi si sa folosesti functia de mai sus
    @Query("select a from Student a where a.id = ?1")
    Student findStudentById(UUID id);
    @Modifying
    @Query("update Student s set s.firstname = COALESCE(?2, s.firstname), " +
            "s.lastname = COALESCE(?3, s.lastname), " +
            "s.email = COALESCE(?4, s.email), " +
            "s.username = COALESCE(?5, s.username), " +
            "s.year = COALESCE(nullif(0, ?6), s.year), " +
            "s.semester = COALESCE(nullif(0, ?7), s.semester), " +
            "s.registrationNumber = COALESCE(?8, s.registrationNumber) " +
            "where s.id = ?1 and (COALESCE(nullif(0, ?6), s.year) between 1 and 3) and (COALESCE(nullif(0, ?7), s.semester) between 1 and 6)")
    void updateStudent(UUID uuid, String firstname, String lastname, String email, String username, Integer year, Integer semester, String registrationNumber);


    Student findByRegistrationNumber(String registrationNumber);
    List<Student> findByYear(int year);
    List<Student> findByGrupaAndYear(String grupa, int year);
    List<Student> findByFirstname(String firstName);
    List<Student> findByLastname(String lastName);

    Student findByEmail(String email);
    Boolean existsByEmail(String email);

}