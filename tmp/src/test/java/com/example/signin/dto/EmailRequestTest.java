package com.example.signin.dto;

import com.example.signin.security.ForgotPasswordRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailRequestTest {

    @Test
    void testSetEmailAndGetEmail() {
        String email = "test@example.com";

        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail(email);

        Assertions.assertEquals(email, requestBody.getEmail());
    }
}