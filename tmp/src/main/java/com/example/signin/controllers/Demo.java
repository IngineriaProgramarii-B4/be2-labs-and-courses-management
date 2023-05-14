package com.example.signin.controllers;

import com.example.signin.security.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class Demo {
    private final JWTGenerator jwtGenerator;

    public Demo(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }


    @GetMapping("/student")
    @PreAuthorize("hasAuthority('STUDENT')")

    public ResponseEntity<String> sayStudent(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            System.out.println(token);
            if(jwtGenerator.validateToken(token))
                return ResponseEntity.ok("This is a STUDENT page");
            else
                return new ResponseEntity<>("Token expired or invalid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Token expired or invalid", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<String> sayTeacher(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            System.out.println(token);
            if(jwtGenerator.validateToken(token))
                return ResponseEntity.ok("This is a TEACHER page");
            else
            return new ResponseEntity<>("Token expired or invalid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Token expired or invalid", HttpStatus.BAD_REQUEST);

    }

}