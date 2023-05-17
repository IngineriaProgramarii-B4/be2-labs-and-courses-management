package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginRequestBodyTest {

    @Test
    void testSettersAndGetters() {
        String email = "example@example.com";
        String password = "password123";

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail(email);
        loginRequestBody.setPassword(password);

        Assertions.assertEquals(email, loginRequestBody.getEmail());
        Assertions.assertEquals(password, loginRequestBody.getPassword());
    }

    @Test
    void testToString() {
        String email = "example@example.com";
        String password = "password123";

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail(email);
        loginRequestBody.setPassword(password);

        String expectedToStringResult = "LoginRequestBody(email=" + email + ", password=" + password + ")";
        Assertions.assertEquals(expectedToStringResult, loginRequestBody.toString());
    }
}