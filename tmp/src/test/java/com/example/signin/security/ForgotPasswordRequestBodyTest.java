package com.example.signin.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ForgotPasswordRequestBodyTest {

    @Test
    public void testGetEmail() {
        String email = "test@example.com";
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail(email);

        String getEmailResult = requestBody.getEmail();

        Assertions.assertEquals(email, getEmailResult);
    }

    @Test
    public void testSetEmail() {
        String email = "test@example.com";
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();

        requestBody.setEmail(email);

        String getEmailResult = requestBody.getEmail();
        Assertions.assertEquals(email, getEmailResult);
    }
}