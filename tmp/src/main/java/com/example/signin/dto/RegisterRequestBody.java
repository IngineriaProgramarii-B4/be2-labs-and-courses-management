package com.example.signin.dto;

import lombok.Data;

@Data
public class RegisterRequestBody {
    private String userId;
    private String email;
    private String password;
    private String confirmPassword;

}
