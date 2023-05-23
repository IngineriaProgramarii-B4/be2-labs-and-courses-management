package com.example.signin.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {

    @Test
    void testTokenCreation() {
        String expectedToken = "testToken";
        Credentials credentials = new Credentials();
        credentials.setUserId("testUserId");

        PasswordResetToken passwordResetToken = new PasswordResetToken(expectedToken, credentials);

        assertEquals(expectedToken, passwordResetToken.getToken());
        assertEquals(credentials, passwordResetToken.getCredentials());
        assertNotNull(passwordResetToken.getExpirationTime());
    }

    @Test
    void testExpirationTimeCalculation() {
        String expectedToken = "testToken";

        PasswordResetToken passwordResetToken = new PasswordResetToken(expectedToken);

        assertEquals(expectedToken, passwordResetToken.getToken());
        assertNotNull(passwordResetToken.getExpirationTime());

        // assert that the expiration time is roughly 10 minutes from now
        long diff = passwordResetToken.getExpirationTime().getTime() - new Date().getTime();
        long diffInMinutes = diff / (60 * 1000);
        assertTrue(diffInMinutes >= 9 && diffInMinutes <= 11);
    }
    @Test
    void testSetTokenIdAndGetTokenId() {
        Long expectedTokenId = 1L;

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setTokenId(expectedTokenId);

        Long actualTokenId = passwordResetToken.getTokenId();

        assertEquals(expectedTokenId, actualTokenId, "The actual tokenId should match the expected tokenId");
    }
}
