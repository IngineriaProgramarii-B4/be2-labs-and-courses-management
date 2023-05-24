package com.example.signin.controllers;

import com.example.signin.security.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DemoTest {

    @InjectMocks
    Demo demo;

    @Mock
    JWTGenerator jwtGenerator;

    @Mock
    HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public final String AUTHHEADERVALID ="Bearer validToken";
    public final String AUTHHEADERINVALID ="Bearer invalidToken";


    @Test
    void testSayStudent() {

        // Arrange
        when(request.getHeader("Authorization")).thenReturn(AUTHHEADERVALID);
        when(jwtGenerator.validateToken("validToken")).thenReturn(true);

        // Act
        ResponseEntity<String> response = demo.sayStudent(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a STUDENT page", response.getBody());
    }

    @Test
    void testSayStudentWithInvalidToken() {

        // Arrange
        when(request.getHeader("Authorization")).thenReturn(AUTHHEADERINVALID);
        when(jwtGenerator.validateToken("invalidToken")).thenReturn(false);

        // Act
        ResponseEntity<String> response = demo.sayStudent(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    void testSayTeacher() {

        // Arrange
        when(request.getHeader("Authorization")).thenReturn(AUTHHEADERVALID);
        when(jwtGenerator.validateToken("validToken")).thenReturn(true);

        // Act
        ResponseEntity<String> response = demo.sayTeacher(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a TEACHER page", response.getBody());
    }

    @Test
    void testSayTeacherWithInvalidToken() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(AUTHHEADERINVALID);
        when(jwtGenerator.validateToken("invalidToken")).thenReturn(false);

        // Act
        ResponseEntity<String> response = demo.sayTeacher(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }
    @Test
    void testSayStudentNoAuthorizationHeader() {

        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<String> response = demo.sayStudent(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    void testSayStudentInvalidAuthorizationHeader() {

        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Act
        ResponseEntity<String> response = demo.sayStudent(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    void testSayTeacherNoAuthorizationHeader() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<String> response = demo.sayTeacher(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    void testSayTeacherInvalidAuthorizationHeader() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Act
        ResponseEntity<String> response = demo.sayTeacher(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }
}