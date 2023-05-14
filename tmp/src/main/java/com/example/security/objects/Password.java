package com.example.security.objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {
    private String pass;
    public Password(String pass) {
        this.pass = pass;
    }
    public String hash() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(pass);
    }

    public String getPass() {
        return pass;
    }
}
