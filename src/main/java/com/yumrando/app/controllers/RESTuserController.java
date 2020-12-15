package com.yumrando.app.controllers;

import com.yumrando.app.models.User;
import com.yumrando.app.repos.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RESTuserController {
    private final UserRepository userDao;

    public RESTuserController(UserRepository userDao){
        this.userDao = userDao;
    }

    @GetMapping("/rest")
    public User dataFind(){
        return userDao.findByUsername("yummy");
    }


}
