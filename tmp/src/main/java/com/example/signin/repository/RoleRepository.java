package com.example.signin.repository;

import com.example.signin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
