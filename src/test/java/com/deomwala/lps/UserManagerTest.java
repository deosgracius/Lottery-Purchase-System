package com.deomwala.lps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    @TempDir Path tmp;
    private UserManager um;

    @BeforeEach
    void setUp() {
        um = new UserManager(tmp.resolve("users.json").toString());
    }

    @Test
    void registersAndAuthenticatesValidUser() {
        assertNull(um.tryCreateAccount("Jane Doe", "jane@example.com", "5550001111", "2000-01-01", "pw123"),
                "valid 18+ registration should succeed (null error)");
        assertEquals(UserType.USER, um.authenticate("jane@example.com", "pw123"));
    }

    @Test
    void rejectsWrongPasswordAndUnknownUser() {
        um.tryCreateAccount("Jane Doe", "jane@example.com", "5550001111", "2000-01-01", "pw123");
        assertNull(um.authenticate("jane@example.com", "wrong-password"));
        assertNull(um.authenticate("nobody@example.com", "pw123"));
    }

    @Test
    void rejectsUnderageRegistration() {
        String tenYearsOld = LocalDate.now().minusYears(10).toString();
        String err = um.tryCreateAccount("Kid", "kid@example.com", "5550001111", tenYearsOld, "pw123");
        assertNotNull(err);
        assertTrue(err.contains("18"), "underage error should mention the 18+ requirement");
    }

    @Test
    void rejectsDuplicateEmail() {
        um.tryCreateAccount("Jane", "dupe@example.com", "5550001111", "2000-01-01", "pw123");
        String err = um.tryCreateAccount("Jane Two", "dupe@example.com", "5550001111", "1999-01-01", "pw456");
        assertNotNull(err);
        assertTrue(err.toLowerCase().contains("exists"));
    }

    @Test
    void rejectsBlankRequiredFields() {
        assertNotNull(um.tryCreateAccount("", "a@b.com", "5550001111", "2000-01-01", "pw"));
    }

    @Test
    void passwordIsSaltedAndHashedNotPlaintext() {
        um.tryCreateAccount("Jane", "jane@example.com", "5550001111", "2000-01-01", "secret");
        User u = um.getUser("jane@example.com");
        assertNotNull(u.salt);
        assertNotEquals("secret", u.pwHash);
        assertEquals(64, u.pwHash.length(), "SHA-256 hex digest is 64 chars");
    }

    @Test
    void resetTokenFlowChangesPassword() {
        um.tryCreateAccount("Jane", "jane@example.com", "5550001111", "2000-01-01", "old-pw");
        String token = um.generateResetToken("jane@example.com");
        assertNotNull(token);
        assertTrue(um.resetPassword(token, "new-pw"));
        assertNull(um.authenticate("jane@example.com", "old-pw"));
        assertEquals(UserType.USER, um.authenticate("jane@example.com", "new-pw"));
    }
}
