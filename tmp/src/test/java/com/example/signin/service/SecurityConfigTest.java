package com.example.signin.service;

import com.example.signin.security.JwtAuthEntryPoint;
import com.example.signin.security.JwtAuthenticationFilter;
import com.example.signin.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthEntryPoint authEntryPoint;


    private SecurityConfig securityConfig;

    @BeforeEach
    public void setup() {
        securityConfig = new SecurityConfig(authEntryPoint);
    }



    @Test
    void testPasswordEncoder() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        // Assert
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
}