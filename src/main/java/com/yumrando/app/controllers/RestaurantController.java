package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class RestaurantController {

    private UserRepository userDao;
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;
    private ReviewRepository reviewDao;

    public RestaurantController(UserRepository userDao, RestaurantRepository restaurantDao, ListRestaurantRepository listDao, ReviewRepository reviewDao) {
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
        this.reviewDao = reviewDao;
    }

    //Reading/Showing all Restaurants
    @GetMapping("/restaurant")
    public String showRestaurants(Model vModel){
        vModel.addAttribute("restaurants", restaurantDao.findAll());
        return "index";
    }


    //Deleting a restaurant from a list
    @PostMapping("/delete/{listId}/{restaurantIdToBeDeleted}")
    public String deleteRestaurantFromList(@PathVariable long listId, @PathVariable long restaurantIdToBeDeleted) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant list = listDao.findAllByUserAndId(userDb, listId);
        Restaurant rest = restaurantDao.findById(restaurantIdToBeDeleted);

        list.removeRestaurantFromList(rest);
        listDao.save(list);

        return "redirect:/" + listId;
    }

    //Deleting Restaurant (Admin)


}
