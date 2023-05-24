package com.example.signin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordResetTest {
    private final String PASSWORD= "newPassword123";

    @Test
    void testSetNewPasswordAndGetNewPassword() {

        // Arrange
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setNewPassword(PASSWORD);

        String actualPassword = passwordResetRequest.getNewPassword();

        // Assert
        assertEquals(PASSWORD, actualPassword, "The actual password should match the expected password");
    }
}