package com.example.signin.service;



import com.example.signin.security.CustomUserDetailsService;
import com.example.signin.security.JWTGenerator;
import com.example.signin.security.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JWTGenerator tokenGenerator;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String TOKEN="sampleToken";

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        // Arrange
        String email = "test@example.com";

        // Act
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        when(tokenGenerator.validateToken(TOKEN)).thenReturn(true);
        when(tokenGenerator.getEmailFromJWT(TOKEN)).thenReturn(email);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenGenerator).validateToken(TOKEN);
        verify(tokenGenerator).getEmailFromJWT(TOKEN);
        verify(customUserDetailsService).loadUserByUsername(email);
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void testDoFilterInternal_invalidToken() throws ServletException, IOException {
        // Act
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        when(tokenGenerator.validateToken(TOKEN)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenGenerator).validateToken(TOKEN);
        verify(tokenGenerator, never()).getEmailFromJWT(TOKEN);
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void testDoFilterInternal_noBearerToken() throws ServletException, IOException {
        // Act
        when(request.getHeader("Authorization")).thenReturn(TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenGenerator, never()).validateToken(TOKEN);
        verify(tokenGenerator, never()).getEmailFromJWT(TOKEN);
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void testDoFilterInternal_nullToken() throws ServletException, IOException {
        // Act
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenGenerator, never()).validateToken(any());
        verify(tokenGenerator, never()).getEmailFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }
}
