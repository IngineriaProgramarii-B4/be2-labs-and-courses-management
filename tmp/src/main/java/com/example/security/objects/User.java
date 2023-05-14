package com.example.security.objects;

import com.example.signin.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends DBObject {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    protected UUID id;
    protected String firstname;
    protected String lastname;
    @Email
    protected String email;
    protected String username;

    @Pattern(regexp = "^(?=.*[!@#$%^&*()])(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "The password must contain at least 8 characters, at least one digit, at least one special symbol and at least one capital letter")
    protected String password;

    @Id
    protected String registrationNumber;

    /*
     * 0 - admin
     * 1 - teacher
     * 2 - student
     * */
    @Min(value=0)
    @Max(value=2)
    protected int type;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "registrationNumber"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    protected User() {

    }



//    protected User(UUID id, String firstname, String lastname, String email, String username, int type) {
//        this.id = id;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.email = email;
//        this.username = username;
//        this.type = type;
//    }

    protected User(String firstname, String lastname, String email, String username, int type,String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.type = type;
    }

//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }


    @Override
    public boolean equals(Object user) {

        if (this == user)
            return true;

        if (user == null || getClass() != user.getClass())
            return false;

        User user1 = (User) user;
        return type == user1.type && Objects.equals(firstname, user1.firstname) && Objects.equals(lastname, user1.lastname) && Objects.equals(email, user1.email) && Objects.equals(username, user1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, email, username, type);
    }
}
