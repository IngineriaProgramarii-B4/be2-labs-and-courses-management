package com.example.signin.service;

import com.example.security.objects.User;
import com.example.signin.model.Credentials;
import com.example.signin.model.PasswordResetToken;
import com.example.signin.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    public void createPasswordResetTokenForUser(Credentials credentials, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, credentials);
        passwordResetTokenRepository.save(passwordRestToken);
    }

    public String validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(passwordResetToken);
        if(passwordToken == null){
            return "Invalid verification token";
        }
        Calendar calendar = Calendar.getInstance();
        if ((passwordToken.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "Link already expired, resend link";
        }
        return "valid";
    }
    public Optional<Credentials> findUserByPasswordToken(String passwordResetToken) {
        PasswordResetToken passwordResetTokenObj = passwordResetTokenRepository.findByToken(passwordResetToken);
        if (passwordResetTokenObj != null) {
            return Optional.ofNullable(passwordResetTokenObj.getCredentials());
        } else {
            return Optional.empty();
        }
    }

    public PasswordResetToken findPasswordResetToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }
}
