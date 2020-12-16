package com.yumrando.app.controllers;

import com.yumrando.app.repos.ListRestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ListRestaurantController {
    private ListRestaurantRepository listRestaurantDao;

    @GetMapping("restaurants/{username}/{listName}")
    private String viewSpecificRestaurantList(@PathVariable String username, @PathVariable String listName, Model vModel){

        return "user/profile";
    }

    //Add List


    //Update List


    //Delete List


    //Different ListName Change
}
