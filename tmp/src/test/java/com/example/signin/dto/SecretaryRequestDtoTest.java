package com.example.signin.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecretaryRequestDtoTest {

    private String REGISTRATIONNUMBER= "123456";
    private final String FIRSTNAME="John";
    private SecretaryRequestDto dto;

    @BeforeEach
    public void setUp() {
        dto = new SecretaryRequestDto();
        dto.setRegistrationNumber(REGISTRATIONNUMBER);
        dto.setFirstname(FIRSTNAME);
    }

    @Test
    public void testGetRegistrationNumber() {
        // Arrange
         String getRegistrationNumberResult = dto.getRegistrationNumber();

        // Assert
        Assertions.assertEquals(REGISTRATIONNUMBER, getRegistrationNumberResult);
    }

    @Test
    public void testGetFirstname() {
        // Arrange
        String getFirstnameResult = dto.getFirstname();
        // Assert
        Assertions.assertEquals(FIRSTNAME, getFirstnameResult);
    }


}