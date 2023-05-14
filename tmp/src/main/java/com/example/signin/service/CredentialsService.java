package com.example.signin.service;

import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import com.example.signin.exception.StudentNotFoundException;
import com.example.signin.model.Credentials;
import com.example.signin.repository.CredentialsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService implements ICredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;


    public CredentialsService(CredentialsRepository credentialsRepository, PasswordResetTokenService passwordResetTokenService, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetToken(token);
    }
    @Override
    public Credentials findUserByPasswordToken(String token) {
        Optional<Credentials> credentials = passwordResetTokenService.findUserByPasswordToken(token);
        if (credentials.isPresent()) {
            return credentials.get();
        } else {
            throw new StudentNotFoundException("Student_auth not found " );
             // or throw an exception
        }
    }
    @Override
    public void createPasswordResetTokenForUser(Credentials credentials, String passwordResetToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(credentials, passwordResetToken);
    }
    public void resetPassword(Credentials credentials, String newPassword) {
        credentials.setPassword(passwordEncoder.encode(newPassword));
        credentialsRepository.save(credentials);
    }
}
