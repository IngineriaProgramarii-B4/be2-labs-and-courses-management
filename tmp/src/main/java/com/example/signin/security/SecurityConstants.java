package com.example.signin.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SecurityConstants {
    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }
    public static final long JWT_EXPIRATION = 3600000;
    public static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final long RESET_TOKEN_EXPIRATION = 900000; // 15 minutes
}
