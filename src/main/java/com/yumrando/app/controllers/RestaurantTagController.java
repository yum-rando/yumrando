package com.yumrando.app.controllers;

import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.TagRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestaurantTagController {
    private UserRepository userDao;
    private TagRepository tagDao;

    public RestaurantTagController (UserRepository userDao, TagRepository tagDao){
        this.userDao = userDao;
        this.tagDao = tagDao;
    }

    @PostMapping("/tags/delete/{tagId}")
    public String deleteRestTag(@PathVariable long tagId) {
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User removeUserTag = userDao.findById(activeUser.getId());
        RestaurantTag removeRestTag = tagDao.findById(tagId);
        removeUserTag.removeFavoriteTagFromUser(removeRestTag);
        userDao.save(removeUserTag);
      return "redirect:/profile";
    }
}
