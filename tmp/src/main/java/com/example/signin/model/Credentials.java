package com.example.signin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.signin.model.Role;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credentials")
@Data
@NoArgsConstructor
public class Credentials {

    @Id
    private String userId;

    @Email
    private String email;
    protected String firstname;
    protected String lastname;
    @Pattern(regexp = "^(?=.*[!@#$%^&*()])(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "The password must contain at least 8 characters, at least one digit, at least one special symbol and at least one capital letter")

    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();
}
