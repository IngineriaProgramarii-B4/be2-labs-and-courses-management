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

    private Credentials CREDENTIALS;

    private final String TOKEN="token";

    private PasswordResetToken PASSWORDRESETTOKEN;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordResetTokenService = new PasswordResetTokenService(passwordResetTokenRepository);

        CREDENTIALS=new Credentials();
        PASSWORDRESETTOKEN = new PasswordResetToken(TOKEN, CREDENTIALS);

    }

    @Test
    void testCreatePasswordResetTokenForUser() {

        // Act
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(PASSWORDRESETTOKEN);

        passwordResetTokenService.createPasswordResetTokenForUser(CREDENTIALS, TOKEN);

        // Assert
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }
    @Test
    void testFindUserByPasswordTokenWithExistingToken() {

        // Act
        when(passwordResetTokenRepository.findByToken(TOKEN)).thenReturn(PASSWORDRESETTOKEN);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(TOKEN);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(CREDENTIALS, result.get());
    }

    @Test
    void testValidatePasswordResetTokenWithValidToken() {
        // Arrange
        String passwordResetToken = "validToken";
        PasswordResetToken validPasswordToken = new PasswordResetToken(passwordResetToken, new Credentials());

        // Act
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(validPasswordToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        // Assert
        assertEquals("valid", result);
    }
    @Test
    void testFindPasswordResetToken() {
        // Arrange
        PasswordResetToken passwordResetTokenObj = new PasswordResetToken(TOKEN, new Credentials());

        // Act
        when(passwordResetTokenRepository.findByToken(TOKEN)).thenReturn(passwordResetTokenObj);

        PasswordResetToken result = passwordResetTokenService.findPasswordResetToken(TOKEN);

        // Assert
        assertEquals(passwordResetTokenObj, result);
    }

    @Test
    void testValidatePasswordResetTokenWithExpiredToken() {
        // Arrange
        String passwordResetToken = "expiredToken";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        PasswordResetToken expiredPasswordToken = new PasswordResetToken(passwordResetToken, new Credentials());
        expiredPasswordToken.setExpirationTime(calendar.getTime());

        // Act
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(expiredPasswordToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        // Assert
        assertEquals("Link already expired, resend link", result);
    }
    @Test
    void testValidatePasswordResetTokenWithInvalidToken() {
        // Arrange
        String passwordResetToken = "invalidToken";

        // Act
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        // Assert
        assertEquals("Invalid verification token", result);
    }



    @Test
    void testFindUserByPasswordTokenWithNonExistingToken() {
        // Arrange
        String passwordResetToken = "nonExistingToken";

        // Act
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);

        // Assert
        assertTrue(result.isEmpty());
    }


}