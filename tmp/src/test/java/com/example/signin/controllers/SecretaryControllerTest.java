package com.example.signin.controllers;

import com.example.signin.dto.SecretaryRequestDto;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.repository.CredentialsRepository;
import com.example.signin.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecretaryControllerTest {

    @Mock
    private CredentialsRepository credentialsRepository;

    @Mock
    private RoleRepository roleRepository;

    private SecretaryController secretaryController;
    public final String ROLE="role";
    public final String REGNUMB1 ="REG-123";
    public final String REGNUMB2 ="REG-456";



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.secretaryController = new SecretaryController(credentialsRepository, roleRepository);
        SecretaryRequestDto secretaryRequestDto = new SecretaryRequestDto();
        secretaryRequestDto.setRegistrationNumber(REGNUMB1);
    }

    private SecretaryRequestDto createSecretaryRequestDto(String registrationNumber, String role) {
        SecretaryRequestDto dto = new SecretaryRequestDto();
        dto.setRegistrationNumber(registrationNumber);
        dto.setRole(role);
        return dto;
    }

    private List<SecretaryRequestDto> createSecretaryRequestDtoList(SecretaryRequestDto... dtos) {
        return new ArrayList<>(Arrays.asList(dtos));
    }


    @Test
    public void addUser_UserAlreadyExists() {
        // Arrange
        SecretaryRequestDto dto = createSecretaryRequestDto(REGNUMB1, null);

        // Act
        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(true);

        ResponseEntity<String> responseEntity = secretaryController.addUser(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User is already in the database!", responseEntity.getBody());
    }


    @Test
    public void addUser_UserDoesNotExistRoleNotValid() {
        // Arrange
        SecretaryRequestDto dto = createSecretaryRequestDto(REGNUMB1, ROLE);

        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(false);
        when(roleRepository.findByName(ROLE)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> responseEntity = secretaryController.addUser(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role not valid!", responseEntity.getBody());
    }

    @Test
    public void addUser_UserDoesNotExistRoleIsValid() {

        // Arrange
        SecretaryRequestDto dto = createSecretaryRequestDto(REGNUMB1, ROLE);

        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(false);
        when(roleRepository.findByName(ROLE)).thenReturn(Optional.of(new Role()));

        // Act
        ResponseEntity<String> responseEntity = secretaryController.addUser(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User added succesfully!", responseEntity.getBody());

        verify(credentialsRepository, times(1)).save(any(Credentials.class));
    }

    @Test
    public void addUsers_OneUserAlreadyExists() {
        // Arrange
        SecretaryRequestDto dto = createSecretaryRequestDto(REGNUMB1, null);
        List<SecretaryRequestDto> dtoList = createSecretaryRequestDtoList(dto);

        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(true);

        // Act
        ResponseEntity<String> responseEntity = secretaryController.addUsers(dtoList);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User REG-123 is already in the database!", responseEntity.getBody());
    }

    @Test
    public void addUsers_AllUsersDoNotExistOneRoleNotValid() {
        // Arrange
        SecretaryRequestDto dto1 = createSecretaryRequestDto(REGNUMB1, ROLE);
        SecretaryRequestDto dto2 = createSecretaryRequestDto(REGNUMB2, "InvalidRole");


        SecretaryRequestDto secretaryRequestDto2 = new SecretaryRequestDto();
        secretaryRequestDto2.setRegistrationNumber(REGNUMB2);
        secretaryRequestDto2.setRole("InvalidRole");

        List<SecretaryRequestDto> secretaryRequestDtoList = new ArrayList<>();
        secretaryRequestDtoList.add(dto1);
        secretaryRequestDtoList.add(dto2);

        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(false);
        when(roleRepository.findByName(ROLE)).thenReturn(Optional.of(new Role()));
        when(credentialsRepository.existsById("REG-456")).thenReturn(false);
        when(roleRepository.findByName("InvalidRole")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> responseEntity = secretaryController.addUsers(secretaryRequestDtoList);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Role InvalidRole not valid!", responseEntity.getBody());
    }

    @Test
    public void addUsers_AllUsersDoNotExistAllRolesValid() {
        // Arrange
        SecretaryRequestDto dto1 = createSecretaryRequestDto(REGNUMB1, ROLE);
        SecretaryRequestDto dto2 = createSecretaryRequestDto(REGNUMB2, ROLE);

        List<SecretaryRequestDto> dtoList = createSecretaryRequestDtoList(dto1, dto2);

        when(credentialsRepository.existsById(REGNUMB1)).thenReturn(false);
        when(roleRepository.findByName(ROLE)).thenReturn(Optional.of(new Role()));
        when(credentialsRepository.existsById(REGNUMB2)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = secretaryController.addUsers(dtoList);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Users added succesfully!", responseEntity.getBody());

        verify(credentialsRepository, times(2)).save(any(Credentials.class));
    }
}