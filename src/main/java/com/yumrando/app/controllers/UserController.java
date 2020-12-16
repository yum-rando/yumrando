package com.yumrando.app.controllers;

import com.yumrando.app.models.User;
import com.yumrando.app.repos.UserRepository;
import com.yumrando.app.repos.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private Users users;
    private PasswordEncoder passwordEncoder;
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

//    @PostMapping("/register")
//    public String saveUser(@ModelAttribute User user, @RequestParam (name = "confirmPassword") String checkPassword){
//        if(user.getPassword().equals(checkPassword)){
//            System.out.println(user.getPassword());
//            String hash = passwordEncoder.encode(user.getPassword());
//            user.setPassword(hash);
//            users.save(user);
//            return "redirect:/index"; // If password equals confirmPassword redirect to index
//        }else{
//            return "redirect:/register";
//        }

    @PostMapping("/register")
    public String saveUser(@RequestParam String username, @RequestParam String password){
        User test = new User();
        test.setUsername(username);
        test.setPassword(password);
                System.out.println(test.getPassword());
                String hash = passwordEncoder.encode(test.getPassword());
                test.setPassword(hash);
                users.save(test);
                return "redirect:/index"; // If password equals confirmPassword redirect to index


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
