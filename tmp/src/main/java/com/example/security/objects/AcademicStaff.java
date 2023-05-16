package com.example.security.objects;

import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class AcademicStaff extends User {
    protected String office;

    protected AcademicStaff() {

    }
    protected AcademicStaff(UUID id, String firstname, String lastname, String email, String username, String office, String registrationNumber) {
        super(id, firstname, lastname, email, username, registrationNumber);
        this.office = office;
    }

    protected AcademicStaff(String firstname, String lastname, String email, String username, String office,  String registrationNumber) {
        super(firstname, lastname, email, username, registrationNumber);
        this.office = office;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
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
