package com.example.security.objects;

import com.example.security.objects.User;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class AcademicStaff extends User {
    protected String office;

    protected AcademicStaff() {

    }
//    protected AcademicStaff(UUID id, String firstname, String lastname, String email, String username, String office, int type) {
//        super(id, firstname, lastname, email, username, type);
//        this.office = office;
//    }

    protected AcademicStaff(String firstname, String lastname, String email, String username, String office, int type,String registrationNumber) {
        super(firstname, lastname, email, username, type,registrationNumber);
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
