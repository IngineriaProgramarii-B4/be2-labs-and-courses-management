package com.example.signin.model;

import com.example.security.objects.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pass_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Credentials credentials;

    public PasswordResetToken(String token, Credentials credentials) {
        super();
        this.token = token;
        this.credentials = credentials;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
