package com.example.user.service;

import com.example.security.objects.Student;
import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import com.example.security.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsersServiceTest {

    @InjectMocks
    UsersService usersService;

    @Mock
    UsersRepository usersRepository;

    private Student student;

    private boolean simulateRepo(Map<String, Object> map) {
        List<Student> expected = List.of(student);

        given(usersRepository.findUsersByParams(
                !map.containsKey("id") ? nullable(UUID.class) : any(UUID.class),
                !map.containsKey("firstname") ? nullable(String.class) : (String) eq(map.get("firstname")),
                !map.containsKey("lastname") ? nullable(String.class) : (String) eq(map.get("lastname")),
                !map.containsKey("email") ? nullable(String.class) : (String) eq(map.get("email")),
                !map.containsKey("username") ? nullable(String.class) : (String) eq(map.get("username"))))
                .willReturn(List.of(student));

        List<User> result = usersService.getUsersByParams(map);

        return result.equals(expected) ? true : false;
    }

    @BeforeEach
    public void setup() {
        student = new Student(
                "FirstnameTest",
                "LastnameTest",
                "EmailTest@gmail.com",
                "UsernameTest",
                1,
                2,
                "RegNumberTest",
                null
        );
    }

    @Test
    void getUsersByParamsIdTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        args.put("id", uuid.toString());

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getUsersByParamsFirstnameTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("firstname", "FirstnameTest");
        UUID uuid = UUID.randomUUID();
        args.put("id", uuid.toString());

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getUsersByParamsLastnameTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("lastname", "LastnameTest");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getUsersByParamsEmailTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("email", "test@gmail.com");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }

    @Test
    void getUsersByParamsUsernameTest() {
        //Given
        Map<String, Object> args = new HashMap<>();
        args.put("username", "UsernameTest");

        //When
        boolean result = simulateRepo(args);

        //Then
        assertTrue(result);
    }
}