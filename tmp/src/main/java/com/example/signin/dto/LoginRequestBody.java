package com.example.signin.dto;

import lombok.Data;

@Data
public class LoginRequestBody {
    private String email;
    private String password;
}
