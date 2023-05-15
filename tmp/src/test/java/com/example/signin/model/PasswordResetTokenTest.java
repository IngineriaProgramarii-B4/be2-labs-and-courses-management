package com.example.signin.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

class PasswordResetTokenTest {

    @Test
    void testTokenExpirationTimeWithUser() {
        testTokenExpirationTime(new PasswordResetToken("testToken", new Credentials()));
    }

    @Test
    void testTokenExpirationTimeWithoutUser() {
        testTokenExpirationTime(new PasswordResetToken("testToken"));
    }

    private void testTokenExpirationTime(PasswordResetToken passwordResetToken) {
        // Get the current time plus 10 minutes
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);
        Date expectedExpirationTime = new Date(calendar.getTime().getTime());

        // Check that the token's expiration time is approximately 10 minutes from now
        // (allowing a margin of error of 1 second to account for the time taken to run the test)
        Assertions.assertTrue(Math.abs(expectedExpirationTime.getTime() - passwordResetToken.getExpirationTime().getTime()) < 1000);
    }
}