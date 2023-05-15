package com.example.user.models;

import com.example.security.objects.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest{


    @Test
    void testToString()
    {
        //
        //Given
        //
        Admin admin = new Admin();

        //
        //When
        //
        admin.setFirstname("testFirstName");
        admin.setLastname("setLastname");

        //
        //Then
        //
        assertEquals("Admin{department='null', office='null', id=, firstname='testFirstName', lastname='setLastname', email='null', username='null'}",admin.toString());

    }
    @Test
    void testHashCode()
    {
        //
        //Given
        //
        Admin admin1 = new Admin();
        Admin admin2 = new Admin();

        //
        //When
        //
        admin1.setFirstname("testFirstName");
        admin1.setLastname("setLastname");
        admin1.setDepartment("testDepartment");
        admin2.setFirstname("testFirstName");
        admin2.setLastname("setLastname");
        admin2.setDepartment("testDepartment");

        //
        //Then
        //
        assertEquals(admin1.hashCode(), admin2.hashCode());

    }

}