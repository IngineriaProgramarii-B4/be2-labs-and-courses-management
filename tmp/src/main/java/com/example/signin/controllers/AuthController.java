package com.example.signin.controllers;

import com.example.security.objects.Student;
import com.example.security.objects.Teacher;
import com.example.security.repositories.StudentsRepository;
import com.example.security.repositories.TeachersRepository;
import com.example.signin.dto.*;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.repository.*;
import com.example.signin.security.*;
import com.example.signin.security.JWTGenerator;
import com.example.signin.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TeachersRepository teachersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final EmailService emailService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CredentialsRepository credentialsRepository;
    private final CredentialsService credentialsService;
    private final StudentsRepository studentsRepository;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, RoleRepository roleRepository, TeachersRepository teachersRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, EmailService emailService, StudentService studentService, TeacherService teacherService, CredentialsRepository credentialsRepository, CredentialsService credentialsService, StudentsRepository studentsRepository) {
        this.authenticationManager = authenticationManager;
        this.teachersRepository = teachersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.emailService = emailService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.credentialsRepository = credentialsRepository;
        this.credentialsService = credentialsService;
        this.studentsRepository = studentsRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestBody loginRequestBody, HttpServletRequest request ) {
        List<Role> roles = new ArrayList<>();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestBody.getEmail(),
                loginRequestBody.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Credentials credentials = credentialsRepository.findByEmail(loginRequestBody.getEmail());
        if(credentials !=null && !credentials.getRoles().isEmpty()){
            roles = credentials.getRoles();
        }
        String token = jwtGenerator.generateToken(authentication, roles);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestBody registerRequestBody) {
        Credentials credentials;
        try {
            if (credentialsRepository.existsByEmail(registerRequestBody.getEmail())) {
                return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
            }

            if (credentialsRepository.existsById(registerRequestBody.getUserId())) {
                credentials = credentialsRepository.findByUserId(registerRequestBody.getUserId());
                if (StringUtils.hasText(credentials.getEmail()) && StringUtils.hasText(credentials.getPassword())) {
                    return new ResponseEntity<>("User is already registered!", HttpStatus.BAD_REQUEST);
                } else {
                    credentials.setEmail(registerRequestBody.getEmail());
                    credentials.setPassword(passwordEncoder.encode(registerRequestBody.getPassword()));
                    credentialsRepository.save(credentials);

                    List<Role> roles = credentials.getRoles();
                    int role =  roles.get(0).getId();
                     if(role == 2){
                        Teacher teacherAuth = new Teacher();
                        teacherAuth.setRegistrationNumber(credentials.getUserId());
                        teacherAuth.setEmail(credentials.getEmail());
                        teacherAuth.setPassword(credentials.getPassword());
                        teachersRepository.save(teacherAuth);
                    }
                    else if(role == 3){
                        Student studentAuth = new Student();
                        studentAuth.setRegistrationNumber(credentials.getUserId());
                        studentAuth.setEmail(credentials.getEmail());
                        studentAuth.setPassword(credentials.getPassword());
                        studentsRepository.save(studentAuth);
                    }

                }
            } else {
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error when saving user!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("/sendMail")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestBody forgotPasswordRequestBody) {
        String email = forgotPasswordRequestBody.getEmail();
        Credentials credentials = credentialsRepository.findByEmail(email);
        if (credentials == null) {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

        try {
            String resetToken = jwtGenerator.generateResetToken(credentials);
            emailService.sendPasswordResetEmail(credentials, resetToken);
            return new ResponseEntity<>("Password reset email sent!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error when sending password reset email!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody EmailRequest emailRequest,
                                       final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {
        System.out.println(emailRequest.getEmail());
        Credentials credentials = credentialsRepository.findByEmail(emailRequest.getEmail());
        String passwordResetUrl = "";
        if (credentials!=null) {
            String passwordResetToken = UUID.randomUUID().toString();
            credentialsService.createPasswordResetTokenForUser(credentials, passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(credentials, applicationUrl(servletRequest), passwordResetToken);
        }

        return passwordResetUrl;
    }
    private String passwordResetEmailLink(Credentials credentials, String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/resetPassword?token="+passwordToken;
        emailService.sendPasswordResetEmail(credentials,url);
        return url;
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String token){
        String tokenVerificationResult = credentialsService.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return new ResponseEntity<>("Invalid token password reset token",HttpStatus.BAD_REQUEST);
        }
        Credentials theUser = credentialsService.findUserByPasswordToken(token);
        if (theUser!=null) {
            credentialsService.resetPassword(theUser, passwordResetRequest.getNewPassword());
            List<Role> roles = theUser.getRoles();
            int role =  roles.get(0).getId();
            if(role == 2){
                Teacher updatedTeacherAuth = teacherService.getTeacherByRegistrationNumber(theUser.getUserId());
                updatedTeacherAuth.setPassword(theUser.getPassword());
                teacherService.addTeacher(updatedTeacherAuth);

            }
            if(role == 3){
                Student updatedStudentAuth = studentService.getStudentByRegistrationNumber(theUser.getUserId());
                updatedStudentAuth.setPassword(theUser.getPassword());
                studentService.addStudent(updatedStudentAuth);

            }
            return new ResponseEntity<>("Password has been reset successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid token password reset token",HttpStatus.BAD_REQUEST);
    }
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +"3000";
    }

}
