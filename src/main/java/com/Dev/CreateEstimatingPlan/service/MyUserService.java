package com.Dev.CreateEstimatingPlan.service;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;

import java.util.List;
import java.util.Optional;

public interface MyUserService {
    List<MyUser> findAllUsers();
    Optional<MyUser> findUserById(Long id);
    MyUser saveUser(MyUser user);

    MyUser editUser(Long id, MyUser user);

    void deleteUserById(Long id);
     void changePassword(Long id, String newPassword);
}
