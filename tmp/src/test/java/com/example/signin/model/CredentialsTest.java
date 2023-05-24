package com.example.signin.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialsTest {
    private final String USERID = "user1";
    private final String EMAIL = "test@example.com";
    private final String FIRSTNAME = "Test";
    private final String LASTNAME = "User";
    private final String PASSWORD = "Password1";


    @Test
    void testSettersAndGetters() {
        // Arrange
        Role role = new Role();
        role.setName(LASTNAME);

        Credentials credentials = new Credentials();
        credentials.setUserId(USERID);
        credentials.setEmail(EMAIL);
        credentials.setFirstname(FIRSTNAME);
        credentials.setLastname(LASTNAME);
        credentials.setPassword(PASSWORD);
        credentials.setRoles(Arrays.asList(role));

        // Assert
        assertEquals(USERID, credentials.getUserId());
        assertEquals(EMAIL, credentials.getEmail());
        assertEquals(FIRSTNAME, credentials.getFirstname());
        assertEquals(LASTNAME, credentials.getLastname());
        assertEquals(PASSWORD, credentials.getPassword());
        assertTrue(credentials.getRoles().contains(role));
    }
}