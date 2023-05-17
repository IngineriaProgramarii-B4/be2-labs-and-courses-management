package com.example.demo;

import com.example.demo.objects.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class MailTest {
    @Test
    public void testGetUsername() {
        String username = "john.doe";
        Mail mail = new Mail(username);

        // Verificați dacă metoda getUsername() returnează valoarea așteptată
        assertEquals(username, mail.getUsername());
    }

    @Test
    public void testGetUsername_DefaultConstructor() {
        Mail mail = new Mail();

        // Verificați dacă metoda getUsername() returnează null atunci când obiectul este creat cu constructorul implicit
        assertNull(mail.getUsername());
    }
}
