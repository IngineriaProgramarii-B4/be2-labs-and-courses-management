package com.example.signin.service;

import com.example.security.objects.User;
import com.example.signin.model.Credentials;
import com.example.signin.model.PasswordResetToken;
import com.example.signin.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetTokenServiceTest {
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordResetTokenService = new PasswordResetTokenService(passwordResetTokenRepository);
    }

    @Test
    void testCreatePasswordResetTokenForUser() {
        Credentials credentials = new Credentials();
        String passwordToken = "token";
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, credentials);

        // Stub the behavior of the repository
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        // Call the method under test
        passwordResetTokenService.createPasswordResetTokenForUser(credentials, passwordToken);

        // Verify that the save method was called once
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void testValidatePasswordResetTokenWithValidToken() {
        String passwordResetToken = "validToken";
        PasswordResetToken validPasswordToken = new PasswordResetToken(passwordResetToken, new Credentials());

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(validPasswordToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        assertEquals("valid", result);
    }

    @Test
    void testValidatePasswordResetTokenWithInvalidToken() {
        String passwordResetToken = "invalidToken";

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        assertEquals("Invalid verification token", result);
    }

    @Test
    void testValidatePasswordResetTokenWithExpiredToken() {
        String passwordResetToken = "expiredToken";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        PasswordResetToken expiredPasswordToken = new PasswordResetToken(passwordResetToken, new Credentials());
        expiredPasswordToken.setExpirationTime(calendar.getTime());

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(expiredPasswordToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        assertEquals("Link already expired, resend link", result);
    }

    @Test
    void testFindUserByPasswordTokenWithExistingToken() {
        String passwordResetToken = "token";
        Credentials credentials = new Credentials();
        PasswordResetToken passwordResetTokenObj = new PasswordResetToken(passwordResetToken, credentials);

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(passwordResetTokenObj);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);

        assertTrue(result.isPresent());
        assertEquals(credentials, result.get());
    }
    @Test
    void testFindUserByPasswordTokenWithNonExistingToken() {
        String passwordResetToken = "nonExistingToken";

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindPasswordResetToken() {
        String passwordResetToken = "token";
        PasswordResetToken passwordResetTokenObj = new PasswordResetToken(passwordResetToken, new Credentials());

        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(passwordResetTokenObj);

        PasswordResetToken result = passwordResetTokenService.findPasswordResetToken(passwordResetToken);

        assertEquals(passwordResetTokenObj, result);
    }
}