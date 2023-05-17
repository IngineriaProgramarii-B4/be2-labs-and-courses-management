package com.example.signin.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ForgotPasswordRequestBodyTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testValidEmail() {
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail("test@example.com");

        Set<ConstraintViolation<ForgotPasswordRequestBody>> violations = validator.validate(requestBody);
        Assertions.assertTrue(violations.isEmpty(), "Email should be valid");
    }

    @Test
    public void testBlankEmail() {
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail("");

        Set<ConstraintViolation<ForgotPasswordRequestBody>> violations = validator.validate(requestBody);
        Assertions.assertFalse(violations.isEmpty(), "Email should not be blank");
    }

    @Test
    public void testInvalidEmailFormat() {
        ForgotPasswordRequestBody requestBody = new ForgotPasswordRequestBody();
        requestBody.setEmail("invalid-email-format");

        Set<ConstraintViolation<ForgotPasswordRequestBody>> violations = validator.validate(requestBody);
        Assertions.assertFalse(violations.isEmpty(), "Email should have a valid format");
    }
}