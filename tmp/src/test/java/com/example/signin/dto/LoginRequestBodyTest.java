package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestBodyTest {
    private String email;
    private String password;
    private LoginRequestBody loginRequestBody;

    private final String PASSWORD= "Password123";

    private final String EMAIL= "example@example.com";

    @BeforeEach
    void setUp() {

        loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail(EMAIL);
        loginRequestBody.setPassword(PASSWORD);
    }
    @Test
    void testSettersAndGetters() {

        // Assert
        Assertions.assertEquals(EMAIL, loginRequestBody.getEmail());
        Assertions.assertEquals(PASSWORD, loginRequestBody.getPassword());
    }

    @Test
    void testToString() {


        // Arrange
         String expectedToStringResult = "LoginRequestBody(email=" + EMAIL + ", password=" + PASSWORD + ")";
        // Assert
        Assertions.assertEquals(expectedToStringResult, loginRequestBody.toString());
    }
}