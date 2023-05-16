package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterRequestBodyTest {

    @Test
    void testSettersAndGetters() {
        String userId = "123";
        String email = "example@example.com";
        String password = "password123";
        String confirmPassword = "password123";

        RegisterRequestBody registerRequestBody = new RegisterRequestBody();
        registerRequestBody.setUserId(userId);
        registerRequestBody.setEmail(email);
        registerRequestBody.setPassword(password);
        registerRequestBody.setConfirmPassword(confirmPassword);

        Assertions.assertEquals(userId, registerRequestBody.getUserId());
        Assertions.assertEquals(email, registerRequestBody.getEmail());
        Assertions.assertEquals(password, registerRequestBody.getPassword());
        Assertions.assertEquals(confirmPassword, registerRequestBody.getConfirmPassword());
    }

    @Test
    void testToString() {
        String userId = "123";
        String email = "example@example.com";
        String password = "password123";
        String confirmPassword = "password123";

        RegisterRequestBody registerRequestBody = new RegisterRequestBody();
        registerRequestBody.setUserId(userId);
        registerRequestBody.setEmail(email);
        registerRequestBody.setPassword(password);
        registerRequestBody.setConfirmPassword(confirmPassword);

        String expectedToStringResult = "RegisterRequestBody(userId=" + userId + ", email=" + email +
                ", password=" + password + ", confirmPassword=" + confirmPassword + ")";
        Assertions.assertEquals(expectedToStringResult, registerRequestBody.toString());
    }
}
