package com.example.signin.security;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenValidationAspectTest {
    @Mock
    private JWTGenerator jwtGenerator;
    @Mock
    private JoinPoint joinPoint;

    private TokenValidationAspect tokenValidationAspect;
    private MockHttpServletRequest request;
    private final String INVALIDTOKEN="invalidToken";

    private final String VALIDTOKEN= "validToken";



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenValidationAspect = new TokenValidationAspect(jwtGenerator);
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void validateToken_throwsException_whenTokenNotFound() {
        // Assert
        assertThrows(RuntimeException.class, () -> tokenValidationAspect.validateToken(joinPoint));
    }

    @Test
    void validateToken_throwsException_whenTokenInvalid() {
        // Arrange
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + INVALIDTOKEN);
        // Act
        when(jwtGenerator.validateToken(INVALIDTOKEN)).thenReturn(false);

        // Assert
        assertThrows(RuntimeException.class, () -> tokenValidationAspect.validateToken(joinPoint));
    }

    @Test
    void validateToken_doesNotThrowException_whenTokenIsValid() throws Throwable {
        // Arrange
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + VALIDTOKEN);
        // Act
        when(jwtGenerator.validateToken(VALIDTOKEN)).thenReturn(true);

        tokenValidationAspect.validateToken(joinPoint);
        // Assert
        verify(jwtGenerator).validateToken(VALIDTOKEN);
    }
}
