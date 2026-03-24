package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.exception.MyUserException;
import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import com.Dev.CreateEstimatingPlan.service.MyUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserServiceImpl implements MyUserService {
    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserServiceImpl(MyUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<MyUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<MyUser> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public MyUser saveUser(MyUser user) {
        Optional<MyUser> savedMyUser = userRepository.findByEmail(user.getEmail());
        if (savedMyUser.isPresent()) {
            throw new MyUserException("Користувач з вказаною поштою : " + user.getEmail() + " вже зареєстрований");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public MyUser editUser(Long id, MyUser user) {
        MyUser myUser = userRepository.findById(id).orElseThrow(() ->
                new MyUserException("Такого користувача не існує")
        );

        Optional<MyUser> withEmail = userRepository.findByEmail(user.getEmail());

        if (withEmail.isPresent() && !withEmail.get().getId().equals(id)) {
            throw new MyUserException("Не можна змінити пошту через те що ця пошта вже використовується : " + user.getEmail());
        }

        user.setId(id);
        user.setPassword(myUser.getPassword());
        return userRepository.save(user);
    }


    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        MyUser myUser = userRepository.findById(id)
                .orElseThrow(() -> new MyUserException("Користувач не знайдений"));

        myUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(myUser);
    }
}
