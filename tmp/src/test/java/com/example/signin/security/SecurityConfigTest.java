package com.example.signin.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private JwtAuthEntryPoint authEntryPoint;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    private SecurityConfig securityConfig;

    @BeforeEach
    public void setup() {
        securityConfig = new SecurityConfig(authEntryPoint);
    }


    @Test
    public void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);

        assertEquals(authenticationManager, result);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }
}