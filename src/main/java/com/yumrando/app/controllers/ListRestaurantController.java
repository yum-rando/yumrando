package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class ListRestaurantController {
    private UserRepository userDao;
    private ListRestaurantRepository listDao;

    public ListRestaurantController(ListRestaurantRepository listDao, UserRepository userDao) {
        this.listDao = listDao;
        this.userDao = userDao;
    }

    //Viewing all restaurants from a specific list --> currently not being used
    @GetMapping("/lists/{listName}/restaurants")
    private String viewSpecificRestaurantList(@PathVariable String listName, Model vModel){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant specificList = listDao.findAllByUserAndName(userDb, listName);
        vModel.addAttribute("specificList", specificList.getRestaurants());
        return "user/profile";
    }

    //DELETING the List from the USER here
    @PostMapping("/delete/lists/{listId}")
    public String deleteListFromUser(@PathVariable long listId){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant list = listDao.findAllByUserAndId(userDb, listId);
        Set<Restaurant> restaurants = list.getRestaurants();
        if (restaurants != null){
            //Removing the restaurants from the Lists 1st to satisfy the many-to-many relationship
            for (Restaurant res : restaurants) {
                res.removeListFromRestaurant(list);
            }
        }
        //Removing the list from the user
        listDao.deleteById(listId); //this works
        return "redirect:/profile";
    }

}
