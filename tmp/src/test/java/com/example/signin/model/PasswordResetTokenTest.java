package com.example.signin.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {

    private String expectedToken;
    private PasswordResetToken passwordResetToken;



    @BeforeEach
    void setUp() {
        expectedToken = "testToken";
        passwordResetToken = new PasswordResetToken(expectedToken);
    }

    @Test
    void testTokenCreation() {
        // Arrange
        Credentials credentials = new Credentials();
        credentials.setUserId("testUserId");

        passwordResetToken = new PasswordResetToken(expectedToken, credentials);

        // Assert
        assertEquals(expectedToken, passwordResetToken.getToken());
        assertEquals(credentials, passwordResetToken.getCredentials());
        assertNotNull(passwordResetToken.getExpirationTime());
    }

    @Test
    void testExpirationTimeCalculation() {
        // Assert
        assertEquals(expectedToken, passwordResetToken.getToken());
        assertNotNull(passwordResetToken.getExpirationTime());

        long diff = passwordResetToken.getExpirationTime().getTime() - new Date().getTime();
        long diffInMinutes = diff / (60 * 1000);
        assertTrue(diffInMinutes >= 9 && diffInMinutes <= 11);
    }

    @Test
    void testSetTokenIdAndGetTokenId() {
        // Arrange
        Long expectedTokenId = 1L;

        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setTokenId(expectedTokenId);

        Long actualTokenId = passwordResetToken.getTokenId();

        // Assert
        assertEquals(expectedTokenId, actualTokenId, "The actual tokenId should match the expected tokenId");
    }
}