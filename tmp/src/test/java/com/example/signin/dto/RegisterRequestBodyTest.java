package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterRequestBodyTest {

    private final String  USERID= "123";
    private final String EMAIL= "example@example.com";
    private final String PASSWORD="password123";
    private final String CONFIRMPASSWORD= "password123";
    private RegisterRequestBody registerRequestBody;

    @BeforeEach
    void setUp() {
        registerRequestBody = new RegisterRequestBody();
        registerRequestBody.setUserId(USERID);
        registerRequestBody.setEmail(EMAIL);
        registerRequestBody.setPassword(PASSWORD);
        registerRequestBody.setConfirmPassword(CONFIRMPASSWORD);
    }
    @Test
    void testSettersAndGetters() {

        // Assert
        Assertions.assertEquals(USERID, registerRequestBody.getUserId());
        Assertions.assertEquals(EMAIL, registerRequestBody.getEmail());
        Assertions.assertEquals(PASSWORD, registerRequestBody.getPassword());
        Assertions.assertEquals(CONFIRMPASSWORD, registerRequestBody.getConfirmPassword());
    }

    @Test
    void testToString() {

        // Arrange
        String expectedToStringResult = "RegisterRequestBody(userId=" + USERID + ", email=" + EMAIL +
                ", password=" + PASSWORD + ", confirmPassword=" + CONFIRMPASSWORD + ")";
        // Assert
        Assertions.assertEquals(expectedToStringResult, registerRequestBody.toString());
    }
}
