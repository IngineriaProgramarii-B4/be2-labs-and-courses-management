package com.example.security.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "admins")
public class Admin extends AcademicStaff {
    private String department;

    public Admin() {

    }

//    public Admin(UUID id, String firstname, String lastname, String email, String username, String office, String department) {
//        super(id, firstname, lastname, email, username, office, 0);
//        this.department = department;
//    }
    public Admin(String firstname, String lastname, String email, String username, String office, String department,String registrationNumber) {

        super(firstname, lastname, email, username, office, 0,registrationNumber);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Admin{" + "department='" + department + '\'' + ", office='" + office + '\'' + ", id=" + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + ", email='" + email + '\'' + ", username='" + username + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object user) {
        return super.equals(user);
    }
}
