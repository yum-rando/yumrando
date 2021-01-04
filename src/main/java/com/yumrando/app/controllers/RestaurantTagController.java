package com.yumrando.app.controllers;

import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.TagRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/Tag/{id}")
    public String restTagCreate(Model model, @PathVariable long id) {
        User authenticUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RestaurantTag userTag = tagDao.findByIdAndAndUsers(id, authenticUser);
        model.addAttribute("userCreatedTag", userTag.getName());
        return "partialsFile/userTagsList";
    }
    @PostMapping("")


}