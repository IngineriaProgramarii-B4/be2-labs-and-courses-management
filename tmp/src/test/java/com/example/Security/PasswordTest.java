package com.example.demo;

import com.example.demo.objects.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PasswordTest {
    @Test
    public void testHash() {
        String password = "password123";
        Password pwd = new Password(password);

        // Verificați dacă metoda hash() returnează o valoare non-null
        assertNotNull(pwd.hash());
    }

    @Test
    public void testGetPass() {
        String password = "password123";
        Password pwd = new Password(password);

        // Verificați dacă metoda getPass() returnează valoarea așteptată
        assertEquals(password, pwd.getPass());
    }
}
