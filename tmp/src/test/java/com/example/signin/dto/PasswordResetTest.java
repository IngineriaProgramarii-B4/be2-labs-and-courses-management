package com.example.signin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordResetTest {

    @Test
    void testSetNewPasswordAndGetNewPassword() {
        String expectedPassword = "newPassword123";

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setNewPassword(expectedPassword);

        String actualPassword = passwordResetRequest.getNewPassword();

        assertEquals(expectedPassword, actualPassword, "The actual password should match the expected password");
    }
}