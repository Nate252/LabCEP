package com.Dev.CreateEstimatingPlan.config.dbconfig;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUser implements CommandLineRunner {
    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            MyUser admin = new MyUser();
            admin.setName("admin");
            admin.setSurname("-");
            admin.setMiddleName("-");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin31245"));
            admin.setRoles("ROLE_ADMIN");
            userRepository.save(admin);
        }
    }
}
