package com.example.signin.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Optional;

import static org.mockito.Mockito.*;
import com.example.signin.model.Credentials;
import com.example.signin.model.PasswordResetToken;
import com.example.signin.repository.PasswordResetTokenRepository;

public class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordResetTokenService passwordResetTokenService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        passwordResetTokenService = new PasswordResetTokenService(passwordResetTokenRepository);
    }

    @Test
    public void testCreatePasswordResetTokenForUser() {
        Credentials credentials = new Credentials();
        String passwordToken = "passwordToken";
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, credentials);

        // Mock the save method of the repository
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> {
            PasswordResetToken savedToken = invocation.getArgument(0);
            Assertions.assertEquals(passwordResetToken.getToken(), savedToken.getToken());
            Assertions.assertEquals(passwordResetToken.getCredentials(), savedToken.getCredentials());
            return savedToken;
        });

        passwordResetTokenService.createPasswordResetTokenForUser(credentials, passwordToken);

        // Verify that the save method was called with the expected token object
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    public void testValidatePasswordResetToken_ValidToken() {
        String passwordResetToken = "validToken";
        PasswordResetToken validToken = new PasswordResetToken(passwordResetToken, new Credentials());
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(validToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("valid", result);
    }

    @Test
    public void testValidatePasswordResetToken_InvalidToken() {
        String passwordResetToken = "invalidToken";
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("Invalid verification token", result);
    }

    @Test
    public void testValidatePasswordResetToken_ExpiredToken() {
        String passwordResetToken = "expiredToken";
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.add(Calendar.DAY_OF_MONTH, -1);
        PasswordResetToken expiredToken = new PasswordResetToken(passwordResetToken, new Credentials());
        expiredToken.setExpirationTime(expirationTime.getTime());
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(expiredToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("Link already expired, resend link", result);
    }

    @Test
    public void testFindUserByPasswordToken() {
        String passwordResetToken = "token";
        Credentials credentials = new Credentials();
        PasswordResetToken token = new PasswordResetToken(passwordResetToken, credentials);
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(token);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);

        Assertions.assertEquals(Optional.of(credentials), result);
    }

//    @Test
//    public void testFindUserByPasswordToken_NonExistingToken() {
//        String passwordResetToken = "nonExistingToken";
//        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);
//
//        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);
//
//        Assertions.assertFalse(result.isPresent());
//    }

    @Test
    public void testFindPasswordResetToken() {
        String token = "token";
        PasswordResetToken expectedToken = new PasswordResetToken(token, new Credentials());
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(expectedToken);

        PasswordResetToken result = passwordResetTokenService.findPasswordResetToken(token);

        Assertions.assertEquals(expectedToken, result);
    }
}