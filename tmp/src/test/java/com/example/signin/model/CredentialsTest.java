package com.example.signin.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialsTest {

    @Test
    void testSettersAndGetters() {
        String userId = "user1";
        String email = "test@example.com";
        String firstname = "Test";
        String lastname = "User";
        String password = "Password1!";
        Role role = new Role();
        role.setName("USER");

        Credentials credentials = new Credentials();
        credentials.setUserId(userId);
        credentials.setEmail(email);
        credentials.setFirstname(firstname);
        credentials.setLastname(lastname);
        credentials.setPassword(password);
        credentials.setRoles(Arrays.asList(role));

        assertEquals(userId, credentials.getUserId());
        assertEquals(email, credentials.getEmail());
        assertEquals(firstname, credentials.getFirstname());
        assertEquals(lastname, credentials.getLastname());
        assertEquals(password, credentials.getPassword());
        assertTrue(credentials.getRoles().contains(role));
    }
}