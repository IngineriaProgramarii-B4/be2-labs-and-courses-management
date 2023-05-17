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

        List<User> expected = List.of(student);

        Map<String, Object> args = new HashMap<>();

        UUID idTest = UUID.randomUUID();

        args.put("id", idTest.toString());

        given(usersRepository.findUsersByParams(
                eq(idTest),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)))
                .willReturn(expected);

        List<User> result = usersService.getUsersByParams(args);

        assertTrue(result.containsAll(expected));
    }

    @Test
    void getUsersByParamsFirstnameTest() {

        List<User> expected = List.of(student);

        Map<String, Object> args = new HashMap<>();

        args.put("firstname", "FirstnameTest");

        args.put("id", "");

        given(usersRepository.findUsersByParams(
                nullable(UUID.class),
                eq("FirstnameTest"),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)))
                .willReturn(expected);

        List<User> result = usersService.getUsersByParams(args);

        assertTrue(result.containsAll(expected));
    }

    @Test
    void getUsersByParamsLastnameTest() {

        List<User> expected = List.of(student);

        Map<String, Object> args = new HashMap<>();

        args.put("lastname", "LastnameTest");

        given(usersRepository.findUsersByParams(
                nullable(UUID.class),
                nullable(String.class),
                eq("LastnameTest"),
                nullable(String.class),
                nullable(String.class)))
                .willReturn(expected);

        List<User> result = usersService.getUsersByParams(args);

        assertTrue(result.containsAll(expected));
    }

    @Test
    void getUsersByParamsEmailTest() {

        List<User> expected = List.of(student);

        Map<String, Object> args = new HashMap<>();

        args.put("email", "test@gmail.com");

        given(usersRepository.findUsersByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                eq("test@gmail.com"),
                nullable(String.class)))
                .willReturn(expected);

        List<User> result = usersService.getUsersByParams(args);

        assertTrue(result.containsAll(expected));
    }

    @Test
    void getUsersByParamsUsernameTest() {

        List<User> expected = List.of(student);

        Map<String, Object> args = new HashMap<>();

        args.put("username", "UsernameTest");

        given(usersRepository.findUsersByParams(
                nullable(UUID.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                eq("UsernameTest")))
                .willReturn(expected);

        List<User> result = usersService.getUsersByParams(args);

        assertTrue(result.containsAll(expected));
    }
}