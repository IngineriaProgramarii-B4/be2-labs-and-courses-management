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

    private Credentials user;

    private final String TOKEN="some_token";
    private final String PASSSWORDRESETTOKEN="reset_token";

    private final String NEWPASSWORD="new_password";
    private final String ENCODEDPASSWORD="encoded_password";



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new CredentialsService(userRepository, passwordResetTokenService, passwordEncoder);
        user = new Credentials();

    }

    @Test
    void testValidatePasswordResetToken() {
        // Act
        when(passwordResetTokenService.validatePasswordResetToken(TOKEN)).thenReturn("Valid");

        String result = userService.validatePasswordResetToken(TOKEN);

        // Assert
        assertEquals("Valid", result);
        verify(passwordResetTokenService, times(1)).validatePasswordResetToken(TOKEN);
    }

    @Test
    void testFindUserByPasswordToken() {
        // Act
        when(passwordResetTokenService.findUserByPasswordToken(TOKEN)).thenReturn(Optional.of(user));

        Credentials result = userService.findUserByPasswordToken(TOKEN);

        // Assert
        assertEquals(user, result);
        verify(passwordResetTokenService, times(1)).findUserByPasswordToken(TOKEN);
    }

    @Test
    void testFindUserByPasswordToken_ThrowsException() {
        // Act
        when(passwordResetTokenService.findUserByPasswordToken(TOKEN)).thenReturn(Optional.empty());

        // Assert
        assertThrows(StudentNotFoundException.class, () -> userService.findUserByPasswordToken(TOKEN));
        verify(passwordResetTokenService, times(1)).findUserByPasswordToken(TOKEN);
    }

    @Test
    void testCreatePasswordResetTokenForUser() {
        // Act
        userService.createPasswordResetTokenForUser(user, PASSSWORDRESETTOKEN);

        // Assert
        verify(passwordResetTokenService, times(1)).createPasswordResetTokenForUser(user, PASSSWORDRESETTOKEN);
    }

    @Test
    void testResetPassword() {
        // Act
        when(passwordEncoder.encode(NEWPASSWORD)).thenReturn(ENCODEDPASSWORD);

        userService.resetPassword(user, NEWPASSWORD);

        // Assert
        assertEquals(ENCODEDPASSWORD, user.getPassword());
        verify(passwordEncoder, times(1)).encode(NEWPASSWORD);
        verify(userRepository, times(1)).save(user);
    }
}
