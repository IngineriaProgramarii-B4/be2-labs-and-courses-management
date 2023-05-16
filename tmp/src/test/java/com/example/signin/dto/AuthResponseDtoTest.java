package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthResponseDtoTest {

    @Test
    void testConstructor() {
        String accessToken = "exampleAccessToken";

        AuthResponseDto authResponseDto = new AuthResponseDto(accessToken);

        Assertions.assertEquals(accessToken, authResponseDto.getAccessToken());
        Assertions.assertEquals("Bearer ", authResponseDto.getTokenType());
    }

    @Test
    void testSettersAndGetters() {
        AuthResponseDto authResponseDto = new AuthResponseDto("exampleAccessToken");

        String newAccessToken = "newAccessToken";
        authResponseDto.setAccessToken(newAccessToken);
        Assertions.assertEquals(newAccessToken, authResponseDto.getAccessToken());

        String newTokenType = "newTokenType";
        authResponseDto.setTokenType(newTokenType);
        Assertions.assertEquals(newTokenType, authResponseDto.getTokenType());
    }

    @Test
    void testToString() {
        String accessToken = "exampleAccessToken";
        AuthResponseDto authResponseDto = new AuthResponseDto(accessToken);

        String expectedToStringResult = "AuthResponseDto(accessToken=" + accessToken + ", tokenType=Bearer )";
        Assertions.assertEquals(expectedToStringResult, authResponseDto.toString());
    }
}