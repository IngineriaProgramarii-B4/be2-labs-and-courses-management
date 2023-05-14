package com.example.security.repositories;

import com.example.security.objects.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    @Query("select a from User a where (cast(?1 as uuid) is null or a.id = ?1) and (?2 is null or a.firstname = ?2) and (?3 is null or a.lastname = ?3) and (?4 is null or a.email = ?4) and (?5 is null or a.username = ?5)")
    List<User> findUsersByParams(UUID id, String firstname, String lastname, String email, String username);

    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByRegistrationNumber(String id);
    Boolean existsByRegistrationNumber(String id);
}