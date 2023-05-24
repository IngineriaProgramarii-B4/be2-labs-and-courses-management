package com.example.signin.controllers;


import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import com.example.security.repositories.StudentsRepository;
import com.example.security.repositories.TeachersRepository;
import com.example.signin.dto.AuthResponseDto;
import com.example.signin.dto.LoginRequestBody;
import com.example.signin.dto.RegisterRequestBody;
import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import com.example.signin.repository.CredentialsRepository;
import com.example.signin.repository.RoleRepository;
import com.example.signin.security.EmailService;
import com.example.signin.security.ForgotPasswordRequestBody;
import com.example.signin.security.JWTGenerator;
import com.example.signin.service.AdminService;
import com.example.signin.service.CredentialsService;
import com.example.signin.service.StudentService;
import com.example.signin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest {


    @Mock
    private TeachersRepository mockTeachersRepository;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private StudentService mockStudentService;
    @Mock
    private RoleRepository mockRoleRepository;


    @Mock
    private TeacherService mockTeacherService;
    @Mock
    private AdminService mockAdminService;

    @Mock
    private AdminsRepository mockAdminsRepository;

    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private CredentialsRepository mockCredentialsRepository;

    @Mock
    private CredentialsService mockCredentialsService;

    @Mock
    private StudentsRepository mockStudentsRepository;
    @Mock
    private JWTGenerator mockJwtGenerator;
    @InjectMocks
    private AuthController authControllerUnderTest;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private RegisterRequestBody registerRequestBody;
    private LoginRequestBody loginRequestBody;
    private ForgotPasswordRequestBody forgotPasswordRequestBody;
    private Credentials credentials;

    private Credentials credentialsNull;
    private Role role;



    public AuthControllerTest() {
    }
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize common objects
        registerRequestBody = new RegisterRequestBody();
        registerRequestBody.setEmail("test@example.com");
        registerRequestBody.setPassword("testPassword");
        registerRequestBody.setUserId("testUserId");

        loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail("test@example.com");
        loginRequestBody.setPassword("testPassword");

        forgotPasswordRequestBody = new ForgotPasswordRequestBody();
        forgotPasswordRequestBody.setEmail("test@example.com");

        role = new Role();
        role.setId(1);

        credentials = new Credentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("testPassword");
        credentials.setRoles(Arrays.asList(role));


        credentialsNull =new Credentials();
        credentialsNull.setEmail(null);
        credentialsNull.setPassword(null);
        credentialsNull.setUserId("testUserId");

        // Initialize AuthController with mocked dependencies
        authControllerUnderTest = new AuthController(
                mockAuthenticationManager,
                mockRoleRepository,
                mockTeachersRepository,
                passwordEncoder,
                mockJwtGenerator,
                mockEmailService,
                mockStudentService,
                mockTeacherService,
                mockAdminService,
                mockCredentialsRepository,
                mockCredentialsService,
                mockStudentsRepository,
                mockAdminsRepository
        );
    }

    @Test
    void testLogin() {


        // Arrange
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        when(mockCredentialsRepository.findByEmail(anyString())).thenReturn(credentials);
        when(mockJwtGenerator.generateToken(any(), any())).thenReturn("testToken");

        // Act
        ResponseEntity<AuthResponseDto> result = authControllerUnderTest.login(loginRequestBody, null);

        // Assert
        assertEquals("testToken", result.getBody().getAccessToken());
        assertEquals(200, result.getStatusCodeValue());
    }
    @Test
    void testRegister_withSaveError() {

        when(mockCredentialsRepository.existsByEmail(registerRequestBody.getEmail())).thenReturn(false);
        when(mockCredentialsRepository.existsById(registerRequestBody.getUserId())).thenReturn(true);
        when(mockCredentialsRepository.findByUserId(registerRequestBody.getUserId())).thenReturn(credentialsNull);
        doThrow(new RuntimeException()).when(mockCredentialsRepository).save(any());

        // Act
        ResponseEntity<String> result = authControllerUnderTest.register(registerRequestBody);

        // Assert
        assertEquals("Error when saving user!", result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
    @Test
    void testRegister_withEmailAlreadyInUse() {

        when(mockCredentialsRepository.existsByEmail(registerRequestBody.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<String> result = authControllerUnderTest.register(registerRequestBody);

        // Assert
        assertEquals("Email is already in use!", result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    void testForgotPassword_userNotFound() {

        when(mockCredentialsRepository.findByEmail(forgotPasswordRequestBody.getEmail())).thenReturn(null);

        // Act
        ResponseEntity<String> result = authControllerUnderTest.forgotPassword(forgotPasswordRequestBody);

        // Assert
        assertEquals("User not found!", result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testForgotPassword_userFoundAndEmailSent() {

        when(mockCredentialsRepository.findByEmail(forgotPasswordRequestBody.getEmail())).thenReturn(credentials);
        when(mockJwtGenerator.generateResetToken(credentials)).thenReturn("testToken");

        // Act
        ResponseEntity<String> result = authControllerUnderTest.forgotPassword(forgotPasswordRequestBody);

        // Assert
        assertEquals("Password reset email sent!", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    void testRegister_withUserAlreadyRegistered() {

        when(mockCredentialsRepository.existsByEmail(registerRequestBody.getEmail())).thenReturn(false);
        when(mockCredentialsRepository.existsById(registerRequestBody.getUserId())).thenReturn(true);
        when(mockCredentialsRepository.findByUserId(registerRequestBody.getUserId())).thenReturn(credentials);

        // Act
        ResponseEntity<String> result = authControllerUnderTest.register(registerRequestBody);

        // Assert
        assertEquals("User is already registered!", result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

}
