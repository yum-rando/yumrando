package com.yumrando.app.controllers;

import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.TagRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class RestaurantTagController {
    private final UserRepository userDao;
    private final ListRestaurantRepository listRestaurantDao;
    private final TagRepository tagDao;
    private final RestaurantRepository restaurantDao;

    //   Dependency Injection for Restaurant Tag Controller
    public RestaurantTagController(UserRepository userDao, ListRestaurantRepository listRestaurantDao, TagRepository tagDao, RestaurantRepository restaurantDao) {
        this.userDao = userDao;
        this.listRestaurantDao = listRestaurantDao;
        this.tagDao = tagDao;
        this.restaurantDao = restaurantDao;
    }


}