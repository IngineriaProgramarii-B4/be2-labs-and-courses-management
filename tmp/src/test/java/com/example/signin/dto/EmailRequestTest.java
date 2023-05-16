package com.example.signin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailRequestTest {

    @Test
    void testSetEmailAndGetEmail() {
        String expectedEmail = "test@example.com";

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(expectedEmail);

        String actualEmail = emailRequest.getEmail();

        assertEquals(expectedEmail, actualEmail, "The actual email should match the expected email");
    }
}