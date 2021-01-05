package com.yumrando.app.controllers;

import com.yumrando.app.models.FriendList;
import com.yumrando.app.repos.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin
@RestController
public class RestUserController {

    private UserRepository userDao;
    private FriendList friendDao;

    public RestUserController(UserRepository userDao, FriendList friendDao){
        this.userDao = userDao;
        this.friendDao = friendDao;
    }
}

//@PostMapping
