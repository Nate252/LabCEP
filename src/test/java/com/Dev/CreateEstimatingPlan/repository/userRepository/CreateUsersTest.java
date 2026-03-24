package com.Dev.CreateEstimatingPlan.repository.userRepository;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateUsersTest {
    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Disabled
    void createTestUsers() {

        for (int i = 1; i <= 50; i++) {
            MyUser user = new MyUser();
            String email = "TestUserMail" + i;
            user.setName("User" + i);
            user.setSurname("-");
            user.setMiddleName("-");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("psw" + i));
            user.setRoles("ROLE_USER");
            userRepository.save(user);
        }
        assertThat(userRepository.count()).isEqualTo(51);
    }

    @Test
    @Disabled
    void deleteAllTestUsers() {
        for (int i = 1; i <= 50; i++) {
            String email = "TestUserMail" + i;
            userRepository.findByEmail(email).ifPresent(userRepository::delete);
        }

    }
}