package com.example.security.objects;

import com.example.signin.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends DBObject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
    protected String firstname;
    protected String lastname;
    @Email
    protected String email;
    protected String username;

    @Pattern(regexp = "^(?=.*[!@#$%^&*()])(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "The password must contain at least 8 characters, at least one digit, at least one special symbol and at least one capital letter")
    protected String password;

    protected String registrationNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "registrationNumber"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    protected User() {

    }

    protected User(UUID id, String firstname, String lastname, String email, String username) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        //Encrypting the email first
//        PasswordEncoder emailEncoder = new BCryptPasswordEncoder();
//        this.email = emailEncoder.encode(email);
        this.username = username;
    }

    protected User(UUID id, String firstname, String lastname, String email, String username, String registrationNumber) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        //Encrypting the email first
//        PasswordEncoder emailEncoder = new BCryptPasswordEncoder();
//        this.email = emailEncoder.encode(email);
        this.username = username;
        this.registrationNumber = registrationNumber;
    }

    protected User(String firstname, String lastname, String email, String username, String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        //Encrypting the email first
//        PasswordEncoder emailEncoder = new BCryptPasswordEncoder();
//        this.email = emailEncoder.encode(email);
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
        //Encrypting the email first
//        PasswordEncoder emailEncoder = new BCryptPasswordEncoder();
//        this.email = emailEncoder.encode(email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object user) {

        if (this == user)
            return true;

        if (user == null || getClass() != user.getClass())
            return false;

        User user1 = (User) user;
        return Objects.equals(id, user1.id) && Objects.equals(firstname, user1.firstname) && Objects.equals(lastname, user1.lastname) && Objects.equals(email, user1.email) && Objects.equals(username, user1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, username);
    }
}
