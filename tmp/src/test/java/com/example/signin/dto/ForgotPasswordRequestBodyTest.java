package com.example.signin.dto;
import com.example.signin.security.ForgotPasswordRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ForgotPasswordRequestBodyTest {

    private ForgotPasswordRequestBody requestBody;

    private final String EMAIL="test@example.com";
    private  String getEmailResult;

    @BeforeEach
    public void setUp() {
        requestBody =new ForgotPasswordRequestBody();
        requestBody.setEmail(EMAIL);
        getEmailResult=requestBody.getEmail();

    }

    @Test
    void testGetEmail() {

        // Assert
        Assertions.assertEquals(EMAIL, getEmailResult);
    }

    @Test
    void testSetEmail() {

        // Assert
        Assertions.assertEquals(EMAIL, getEmailResult);
    }
}