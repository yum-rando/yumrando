package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class RestaurantController {
    private UserRepository userDao;
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;

    //Reading/Showing all Restaurants
    @GetMapping("/restaurant")
    public String showRestaurants(Model vModel){
        vModel.addAttribute("restaurants", restaurantDao.findAll());
        return "index";
    }

    //Reading/Selecting 10 most recent Restaurant Choices
    @GetMapping("/restaurants/{username}/most-recent")
    public String showMostRecent(@PathVariable String username, Model vModel){
        User user = userDao.findByUsername(username);

        //Need to review all the restaurants to sort out by the 10 most recent chosen
        //Used the stream method to further apply limit of 10 option to the list of restaurants
        List<Restaurant> mostRecent = restaurantDao.findAll(Sort.by("chosen_time").descending())
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        //Send to the front
        vModel.addAttribute("mostRecent", mostRecent);

        //Issue --> How to tie this in with the specific user and their choices for restaurants
            //Might have to go thru the Review table since that is a direct connection between Users and Restaurants without a list

        return "user/profile";
    }

    //Randomizer Button --> POST REQUEST
    @PostMapping("/restaurant/randomizer")
    public String randomizerBtn(){
        List<Restaurant> restaurants = restaurantDao.findAll();
        Random random = new Random();
        random
        return "index";
    }


    //Adding Restaurant //(Any user, especially if new restaurant) Double Check to make sure this restaurant isn't already in the database



    //Deleting Restaurant (Admin)


}
