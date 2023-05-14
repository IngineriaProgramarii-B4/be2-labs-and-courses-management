package com.example.signin.service;


import com.example.security.objects.User;
import com.example.signin.model.Credentials;

public interface ICredentialsService {
    String validatePasswordResetToken(String token);

    Credentials findUserByPasswordToken(String token);

    void createPasswordResetTokenForUser(Credentials credentials, String passwordResetToken);
}
