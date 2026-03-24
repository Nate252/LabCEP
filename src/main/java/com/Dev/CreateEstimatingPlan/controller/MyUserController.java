package com.Dev.CreateEstimatingPlan.controller;

import com.Dev.CreateEstimatingPlan.exception.MyUserException;
import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.serviceImpl.MyUserServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MyUserController {
    private final MyUserServiceImpl userService;

    public MyUserController(MyUserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/showUsers")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "MyUser/showUsers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/addUserShow")
    public String addUserShow(Model model) {
        model.addAttribute("newUser", new MyUser());
        return "MyUser/addUser";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") MyUser newUser, Model model) {
        try {
            userService.saveUser(newUser);
        } catch (MyUserException e) {
            model.addAttribute("error", e.getMessage());
            return "MyUser/addUser";
        }
        return "redirect:/showUsers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/editUserShow/{id}")
    public String editUserShow(@PathVariable Long id, Model model) {
        Optional<MyUser> myUser = userService.findUserById(id);
        if (myUser.isPresent()) {
            MyUser user = myUser.get();
            model.addAttribute("editUser", user);
            return "MyUser/editUser";
        } else {
            return "redirect:/showUsers";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute("editUser") MyUser editUser, Model model) {

        try {
            userService.editUser(id, editUser);
        } catch (MyUserException e) {
            model.addAttribute("error", e.getMessage());
            return "MyUser/editUser";
        }
        return "redirect:/showUsers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/showUsers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/changeUserPswShow/{id}")
    public String changeUserPswShow(@PathVariable Long id, Model model) {

        model.addAttribute("userId", id);

        return "MyUser/changeUserPassword";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/changeUserPsw/{id}")
    public String changeUserPsw(@PathVariable Long id, @RequestParam("psw") String newPassword, Model model) {

        try {
            userService.changePassword(id, newPassword);
        } catch (MyUserException e) {
            model.addAttribute("error", e.getMessage());
            return "MyUser/changeUserPassword";
        }
        return "redirect:/showUsers";
    }
}

