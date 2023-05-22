package com.example.signin.dto;
import com.example.signin.security.ForgotPasswordRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ForgotPasswordRequestBodyTest {

    @Test
    void testGetEmail() {
        String email = "test@example.com";
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail(email);

        String getEmailResult = requestBody.getEmail();

        Assertions.assertEquals(email, getEmailResult);
    }

    @Test
    void testSetEmail() {
        String email = "test@example.com";
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();

        requestBody.setEmail(email);

        String getEmailResult = requestBody.getEmail();
        Assertions.assertEquals(email, getEmailResult);
    }
}