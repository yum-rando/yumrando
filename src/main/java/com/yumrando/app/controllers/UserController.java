package com.yumrando.app.controllers;

import com.yumrando.app.models.User;
import com.yumrando.app.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    private final UserRepository userDao;

    public UserController(UserRepository userDao){
        this.userDao = userDao;
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
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
