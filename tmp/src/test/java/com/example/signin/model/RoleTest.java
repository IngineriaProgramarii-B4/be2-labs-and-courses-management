package com.example.signin.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void testSettersAndGetters() {
        int id = 1;
        String name = "ROLE_ADMIN";

        Role role = new Role();
        role.setId(id);
        role.setName(name);

        Assertions.assertEquals(id, role.getId());
        Assertions.assertEquals(name, role.getName());
    }

    @Test
    void testToString() {
        int id = 1;
        String name = "ROLE_ADMIN";

        Role role = new Role();
        role.setId(id);
        role.setName(name);

        String expectedToStringResult = "Role(id=" + id + ", name=" + name + ")";
        Assertions.assertEquals(expectedToStringResult, role.toString());
    }
}