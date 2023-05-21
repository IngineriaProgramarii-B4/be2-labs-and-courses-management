package com.example.signin.controllers;

import com.example.signin.dto.SecretaryRequestDto;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.repository.CredentialsRepository;
import com.example.signin.repository.RoleRepository;
import com.example.signin.service.CredentialsService;
import com.example.signin.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/v1/secretary")
public class SecretaryController {
private final CredentialsService credentialsService;
private final CredentialsRepository credentialsRepository;
private final RoleRepository roleRepository;

    public SecretaryController(CredentialsService credentialsService, CredentialsRepository credentialsRepository, RoleRepository roleRepository) {
        this.credentialsService = credentialsService;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addUser(@RequestBody SecretaryRequestDto secretaryRequestDto) {
        if (credentialsRepository.existsById(secretaryRequestDto.getRegistrationNumber())) {
            return new ResponseEntity<>("User is already in the database!", HttpStatus.BAD_REQUEST);
        }
        Credentials credentials = new Credentials();
        credentials.setUserId(secretaryRequestDto.getRegistrationNumber());
        credentials.setFirstname(secretaryRequestDto.getFirstname());
        credentials.setLastname(secretaryRequestDto.getLastname());
        Optional<Role> roleOptional = roleRepository.findByName(secretaryRequestDto.getRole());
        if (roleOptional.isPresent()) {
            credentials.getRoles().add(roleOptional.get());
            credentialsRepository.save(credentials);
            return new ResponseEntity<>("User added succesfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Role not valid!", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addUsers(@RequestBody List<SecretaryRequestDto> secretaryRequestDtoList) {
        List<Credentials> credentialsToAdd = new ArrayList<>();
        for (int i = 0; i < secretaryRequestDtoList.size() ; i++) {
            SecretaryRequestDto secretaryRequestDto = secretaryRequestDtoList.get(i);
            if (credentialsRepository.existsById(secretaryRequestDto.getRegistrationNumber())) {
                return new ResponseEntity<>("User " + secretaryRequestDto.getRegistrationNumber() + " is already in the database!", HttpStatus.BAD_REQUEST);
            }
            Credentials credentials = new Credentials();
            credentials.setUserId(secretaryRequestDto.getRegistrationNumber());
            credentials.setFirstname(secretaryRequestDto.getFirstname());
            credentials.setLastname(secretaryRequestDto.getLastname());
            Optional<Role> roleOptional = roleRepository.findByName(secretaryRequestDto.getRole());
            System.out.println(secretaryRequestDto.getRegistrationNumber());
            System.out.println(secretaryRequestDto.getRole());
            if (roleOptional.isPresent()) {
                credentials.getRoles().add(roleOptional.get());
                credentialsToAdd.add(credentials);
            } else {
                return new ResponseEntity<>("Role " + secretaryRequestDto.getRole() + " not valid!", HttpStatus.BAD_REQUEST);
            }
        }
        for (Credentials credentials : credentialsToAdd) {
            credentialsRepository.save(credentials);
        }
        return new ResponseEntity<>("Users added succesfully!", HttpStatus.CREATED);
    }



}
