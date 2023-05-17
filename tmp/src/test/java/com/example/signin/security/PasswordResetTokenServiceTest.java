package com.example.signin.security;

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
import com.example.signin.service.PasswordResetTokenService;
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

        passwordResetTokenService.createPasswordResetTokenForUser(credentials, passwordToken);

        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    public void testValidatePasswordResetToken_InvalidToken() {
        String passwordResetToken = "invalidToken";
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(null);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("Invalid verification token", result);
    }

    @Test
    public void testValidatePasswordResetToken_LinkExpired() {
        String passwordResetToken = "validToken";
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setExpirationTime(Calendar.getInstance().getTime());
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(expiredToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("Link already expired, resend link", result);
    }

    @Test
    public void testValidatePasswordResetToken_ValidToken() {
        String passwordResetToken = "validToken";
        PasswordResetToken validToken = new PasswordResetToken();
        // Set the expiration time to a future date (e.g., one day from now)
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.add(Calendar.DAY_OF_MONTH, 1);
        validToken.setExpirationTime(expirationTime.getTime());
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(validToken);

        String result = passwordResetTokenService.validatePasswordResetToken(passwordResetToken);

        Assertions.assertEquals("valid", result);
    }

    @Test
    public void testFindUserByPasswordToken() {
        String passwordResetToken = "passwordToken";
        Credentials credentials = new Credentials();
        PasswordResetToken token = new PasswordResetToken(passwordResetToken, credentials);
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(token);

        Optional<Credentials> result = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);

        Assertions.assertEquals(credentials, result.orElse(null));
    }

    @Test
    public void testFindPasswordResetToken() {
        String passwordResetToken = "passwordToken";
        PasswordResetToken token = new PasswordResetToken(passwordResetToken, new Credentials());
        when(passwordResetTokenRepository.findByToken(passwordResetToken)).thenReturn(token);

        PasswordResetToken result = passwordResetTokenService.findPasswordResetToken(passwordResetToken);

        Assertions.assertEquals(token, result);
    }
}