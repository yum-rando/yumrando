package com.yumrando.app.controllers;

import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import com.yumrando.app.repos.UserTagRepository;

public class UserTagController {
    private RestaurantRepository restDao;
    private UserRepository userDao;
    private UserTagRepository userTagDao;

    public UserTagController(RestaurantRepository restDao, UserRepository userDao, UserTagRepository userTagDao){
        this.restDao = restDao;
        this.userDao = userDao;
        this.userTagDao = userTagDao;
    }

}
