package com.example.signin.dto;

import com.example.signin.controllers.AuthController;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.security.ForgotPasswordRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

class AuthResponseDtoTest {

    public final String ACCESSTOKEN="exampleAccessToken";

    private AuthResponseDto authResponseDto;


    @BeforeEach
    public void setUp() {
        authResponseDto =new AuthResponseDto(ACCESSTOKEN);

    }

    @Test
    void testConstructor() {

        // Assert
        Assertions.assertEquals(ACCESSTOKEN, authResponseDto.getAccessToken());
        Assertions.assertEquals("Bearer ", authResponseDto.getTokenType());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        authResponseDto.setAccessToken(ACCESSTOKEN);
        // Assert
        Assertions.assertEquals(ACCESSTOKEN, authResponseDto.getAccessToken());

        // Arrange
        authResponseDto.setTokenType(ACCESSTOKEN);
        // Assert
        Assertions.assertEquals(ACCESSTOKEN, authResponseDto.getTokenType());

    }

    @Test
    void testToString() {
        // Arrange
        String expectedToStringResult = "AuthResponseDto(accessToken=" + ACCESSTOKEN + ", tokenType=Bearer )";
        // Assert
        Assertions.assertEquals(expectedToStringResult, authResponseDto.toString());
    }
}