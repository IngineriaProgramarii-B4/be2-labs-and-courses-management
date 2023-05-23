package com.example.signin.service;

import com.example.signin.exception.StudentNotFoundException;

import com.example.signin.model.Credentials;
import com.example.signin.repository.CredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialsServiceTest {

    private CredentialsService userService;

    @Mock
    private CredentialsRepository userRepository;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new CredentialsService(userRepository, passwordResetTokenService, passwordEncoder);
    }

    @Test
    void testValidatePasswordResetToken() {
        // Mock the behavior of passwordResetTokenService
        String token = "some_token";
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("Valid");

        String result = userService.validatePasswordResetToken(token);

        assertEquals("Valid", result);
        verify(passwordResetTokenService, times(1)).validatePasswordResetToken(token);
    }

    @Test
    void testFindUserByPasswordToken() {
        // Mock the behavior of passwordResetTokenService
        String token = "some_token";
        Credentials user = new Credentials();
        when(passwordResetTokenService.findUserByPasswordToken(token)).thenReturn(Optional.of(user));

        Credentials result = userService.findUserByPasswordToken(token);

        assertEquals(user, result);
        verify(passwordResetTokenService, times(1)).findUserByPasswordToken(token);
    }

    @Test
    void testFindUserByPasswordToken_ThrowsException() {
        // Mock the behavior of passwordResetTokenService
        String token = "non_existing_token";
        when(passwordResetTokenService.findUserByPasswordToken(token)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> userService.findUserByPasswordToken(token));
        verify(passwordResetTokenService, times(1)).findUserByPasswordToken(token);
    }

    @Test
    void testCreatePasswordResetTokenForUser() {
        // Mock the behavior of passwordResetTokenService
        Credentials user = new Credentials();
        String passwordResetToken = "reset_token";

        userService.createPasswordResetTokenForUser(user, passwordResetToken);

        verify(passwordResetTokenService, times(1)).createPasswordResetTokenForUser(user, passwordResetToken);
    }

    @Test
    void testResetPassword() {
        // Mock the behavior of passwordEncoder and userRepository
        Credentials user = new Credentials();
        String newPassword = "new_password";
        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userService.resetPassword(user, newPassword);

        assertEquals(encodedPassword, user.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(user);
    }
}
