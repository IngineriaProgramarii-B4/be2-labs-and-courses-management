package com.example.security.objects;

import com.example.signin.model.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    public void testToString(){
        Date createdAt = new Date();
        Date updatedAt = new Date();
        User user = new User();

        user.setFirstname("firstName");
        user.setLastname("lastName");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        user.setPassword("parola");
        user.setRegistrationNumber("3109RSL");
        Role role1 = new Role();
        Role role2 = new Role();

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        user.setRoles(roles);

        String expectedString = "User{" +
                "id=" + user.getId() +
                ", firstname='" + "firstName" + '\'' +
                ", lastname='" + "lastName" + '\'' +
                ", email='" + "email@gmail.com" + '\'' +
                ", username='" + "username" + '\'' +
                ", password='" + "parola" + '\'' +
                ", registrationNumber='" + "3109RSL" + '\'' +
                ", roles=" + roles +
                '}';

        assertEquals(expectedString, user.toString());
    }
}
