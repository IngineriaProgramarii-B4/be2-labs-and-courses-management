package com.example.signin.security;

import com.example.signin.model.Credentials;
import com.example.signin.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class JWTGenerator {
    public String generateToken(Authentication authentication, List<Role> roles)
    {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .claim("role",roles.get(0).getName())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SecurityConstants.JWT_SECRET,SignatureAlgorithm.HS512)
                .compact();
    }
    public String getEmailFromJWT(String token)
    {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder().setSigningKey(SecurityConstants.JWT_SECRET).build().parseClaimsJws(token);
            return true;
        }catch (Exception ex)
        {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public String generateResetToken(Credentials credentials) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.RESET_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(credentials.getEmail())
                .claim("userId", credentials.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SecurityConstants.JWT_SECRET, SignatureAlgorithm.HS512)
                .compact();
    }
}
