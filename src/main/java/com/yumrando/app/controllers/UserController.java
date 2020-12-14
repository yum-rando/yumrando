package com.yumrando.app.controllers;

import com.yumrando.app.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    private final UserRepository userDao;

    public UserController(UserRepository userDao){
        this.userDao = userDao;
    }

    @GetMapping("/register")
    @ResponseBody
    public String showRegistrationPage() {
        return "Registration page!";
    }

    @GetMapping("/profile")
    @ResponseBody
    public String showProfile() {
        return "Profile page goes here!";
    }

    @GetMapping("/login")
    @ResponseBody
    public String showLogInPage() {
        return "Login form goes here";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String executeLogout() {
        return "redirect:/index";
    }
}
