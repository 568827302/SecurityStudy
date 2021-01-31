package com.example.demo.security.auth.ldap;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@SpringBootTest
class LDAPUserRepoTest {
    @Autowired
    LDAPUserRepo ldapUserRepo;

    @Test
    void giveTrueUsernameAndTruePassword_shouldPass() {
        String username = "zhaoliu";
        String password = "123";
        val user = ldapUserRepo.findOptionalByUsernameAndPassword(username, password);
        assertTrue(user.isPresent());
    }
    @Test
    void giveTrueUsernameAndWrongPassword_shouldPass() {
        String username = "zhaoliu";
        String password = "bad_password";
        val user = ldapUserRepo.findOptionalByUsernameAndPassword(username, password);
        assertTrue(user.isEmpty());
    }
}