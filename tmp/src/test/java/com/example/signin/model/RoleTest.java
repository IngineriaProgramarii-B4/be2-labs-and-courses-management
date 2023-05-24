package com.example.signin.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private final int id = 1;
    private final String ROLENAME = "ROLE_ADMIN";

    private Role role;
    @BeforeEach
    void setUp() {

        role =new Role();
        role.setId(id);
        role.setName(ROLENAME);
    }

    @Test
    void testSettersAndGetters() {

        // Assert
        Assertions.assertEquals(id, role.getId());
        Assertions.assertEquals(ROLENAME, role.getName());
    }

    @Test
    void testToString() {
        // Arrange
        String expectedToStringResult = "Role(id=" + id + ", name=" + ROLENAME + ")";
        // Assert
        Assertions.assertEquals(expectedToStringResult, role.toString());
    }
}