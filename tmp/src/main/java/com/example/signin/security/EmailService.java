package com.example.signin.security;

import com.example.catalog.models.Grade;
import com.example.signin.model.Credentials;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(Credentials credentials, String url) throws MessagingException, UnsupportedEncodingException {

        String subject = "Password Reset Request Verification";
        String senderName = "Labs and Courses Portal Service";
        String mailContent = "<p> Hi, </p>"+
                "<p><b>You recently requested to reset your password.</b>"+"" +
                " Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("noreply_b4@gmail.com", senderName);
        messageHelper.setTo(credentials.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);

    }

    public void sendGradeAddedEmail(String email, Grade grade) {
        String subject = "You have a new grade!";
        String senderName = "Labs and Courses Portal Service";
        String mailContent = "<p> Hi, </p>" +
                "<p><b>You've been evaluated with " + grade.getValue() + " in " + grade.getSubject() + ".<b>" +
                ".\n Have a great day!";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        try {
            messageHelper.setFrom("noreply_b4@gmail.com", senderName);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);
        }
        catch (Exception e) {
            e.addSuppressed(e);
        }
        mailSender.send(message);
    }

}
