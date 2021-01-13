package com.yumrando.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AuthenticationController {
        @GetMapping("/login")
        public String showLoginForm(Principal user) {
            if (user != null){
                return "redirect:/";
            }
            return "user/login";
        }
}
