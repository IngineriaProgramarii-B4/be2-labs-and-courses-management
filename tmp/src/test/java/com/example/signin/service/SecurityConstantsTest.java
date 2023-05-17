package com.example.signin.service;

import com.example.signin.security.SecurityConstants;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConstantsTest {

    @Test
    void testJwtExpiration() {
        long expectedJwtExpiration = 3600000;
        assertEquals(SecurityConstants.JWT_EXPIRATION, expectedJwtExpiration);
    }

    @Test
    void testJwtSecret() {
        SecretKey expectedSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        assertNotNull(SecurityConstants.JWT_SECRET);
        assertEquals(expectedSecretKey.getAlgorithm(), SecurityConstants.JWT_SECRET.getAlgorithm());
    }

    @Test
    void testResetTokenExpiration() {
        long expectedResetTokenExpiration = 900000;
        assertEquals(SecurityConstants.RESET_TOKEN_EXPIRATION, expectedResetTokenExpiration);
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<SecurityConstants> constructor = SecurityConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true); // make the constructor accessible

        try {
            constructor.newInstance();
            fail("Expected exception not thrown");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
        }
    }
}