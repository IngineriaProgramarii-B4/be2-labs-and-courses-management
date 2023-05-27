package com.example.signin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailRequestTest {
    private final String EMAIL="test@example.com";

    @Test
    void testSetEmailAndGetEmail() {

        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(EMAIL);

        String actualEmail = emailRequest.getEmail();

        // Assert
        assertEquals(EMAIL, actualEmail, "The actual email should match the expected email");
    }
}