package com.example.signin.repository;

import com.example.signin.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials,String> {
    Credentials findByEmail(String email);
    Boolean existsByEmail(String email);
    Credentials findByUserId(String id);

}
