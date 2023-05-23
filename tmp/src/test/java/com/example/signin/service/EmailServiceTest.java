package com.example.signin.service;


import com.example.signin.model.Credentials;
import com.example.signin.security.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    private Credentials user;
    private String url;

    @BeforeEach
     void setup() {
        user = new Credentials();
        user.setEmail("test@example.com");

        url = "http://example.com/resetPassword";
    }

    @Test
     void testSendPasswordResetEmail() throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendPasswordResetEmail(user, url);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
