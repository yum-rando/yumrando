package com.yumrando.app.controllers;

import com.yumrando.app.models.User;
import com.yumrando.app.repos.UserRepository;
import com.yumrando.app.repos.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private Users users;
    private PasswordEncoder passwordEncoder;
    private final UserRepository userDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, Users users) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
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

    @PostMapping("/register")
    public String saveUser(
            @Valid User user,
            @RequestParam(name = "confirmPassword") String checkPassword,
            Errors validation,
            Model model) {
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("user", user);
            System.out.println(validation);
            return "user/register";
        } else if (user.getPassword().equals(checkPassword)) {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            users.save(user);
            return "redirect:/index"; // If password equals confirmPassword redirect to index)
        }
        return "user/register";

    }


    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String executeLogout() {
        return "redirect:/index";
    }

}
