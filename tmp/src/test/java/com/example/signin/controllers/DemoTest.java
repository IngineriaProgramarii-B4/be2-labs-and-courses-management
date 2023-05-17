package com.example.signin.controllers;

import com.example.signin.security.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DemoTest {

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

    @Test
    public void testSayStudent() {
        String authHeader = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtGenerator.validateToken("validToken")).thenReturn(true);

        ResponseEntity<String> response = demo.sayStudent(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a STUDENT page", response.getBody());
    }

    @Test
    public void testSayStudentWithInvalidToken() {
        String authHeader = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtGenerator.validateToken("invalidToken")).thenReturn(false);

        ResponseEntity<String> response = demo.sayStudent(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    public void testSayTeacher() {
        String authHeader = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtGenerator.validateToken("validToken")).thenReturn(true);

        ResponseEntity<String> response = demo.sayTeacher(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a TEACHER page", response.getBody());
    }

    @Test
    public void testSayTeacherWithInvalidToken() {
        String authHeader = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtGenerator.validateToken("invalidToken")).thenReturn(false);

        ResponseEntity<String> response = demo.sayTeacher(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }
    @Test
    public void testSayStudentNoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<String> response = demo.sayStudent(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    public void testSayStudentInvalidAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        ResponseEntity<String> response = demo.sayStudent(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    public void testSayTeacherNoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<String> response = demo.sayTeacher(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }

    @Test
    public void testSayTeacherInvalidAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        ResponseEntity<String> response = demo.sayTeacher(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expired or invalid", response.getBody());
    }
}