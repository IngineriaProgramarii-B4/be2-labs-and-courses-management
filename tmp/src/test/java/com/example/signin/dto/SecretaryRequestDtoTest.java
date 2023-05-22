package com.example.signin.dto;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SecretaryRequestDtoTest {

    @Test
    public void testGetRegistrationNumber() {
        String registrationNumber = "123456";
        SecretaryRequestDto dto = new SecretaryRequestDto();
        dto.setRegistrationNumber(registrationNumber);

        String getRegistrationNumberResult = dto.getRegistrationNumber();

        Assertions.assertEquals(registrationNumber, getRegistrationNumberResult);
    }

    @Test
    public void testSetRegistrationNumber() {
        String registrationNumber = "123456";
        SecretaryRequestDto dto = new SecretaryRequestDto();

        dto.setRegistrationNumber(registrationNumber);

        String getRegistrationNumberResult = dto.getRegistrationNumber();
        Assertions.assertEquals(registrationNumber, getRegistrationNumberResult);
    }

    @Test
    public void testGetFirstname() {
        String firstname = "John";
        SecretaryRequestDto dto = new SecretaryRequestDto();
        dto.setFirstname(firstname);

        String getFirstnameResult = dto.getFirstname();

        Assertions.assertEquals(firstname, getFirstnameResult);
    }

    @Test
    public void testSetFirstname() {
        String firstname = "John";
        SecretaryRequestDto dto = new SecretaryRequestDto();

        dto.setFirstname(firstname);

        String getFirstnameResult = dto.getFirstname();
        Assertions.assertEquals(firstname, getFirstnameResult);
    }

}
