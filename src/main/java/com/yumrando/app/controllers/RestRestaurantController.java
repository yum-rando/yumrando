package com.yumrando.app.controllers;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.TagRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RestRestaurantController {
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;
    private UserRepository userDao;
    private TagRepository tagDao;

    public RestRestaurantController(RestaurantRepository restaurantDao, ListRestaurantRepository listDao, UserRepository userDao, TagRepository tagDao) {
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
        this.userDao = userDao;
        this.tagDao = tagDao;
    }

    @CrossOrigin
    @PostMapping("restaurants/lists/{id}")
    Set<Restaurant> restaurants (@RequestBody Restaurant restaurantToBeSaved, @PathVariable long id){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listDao.findAllByUser(userDb);
        ListRestaurant listRes = listDao.findAllByUserAndId(userDb, id);
        String restName = restaurantToBeSaved.getName();
        if (restName.isEmpty()){
            return listRes.getRestaurants();
        }

        String restZipcode = restaurantToBeSaved.getZipcode();

        //Find a restaurant by the ZIPCODE and RESTAURANT NAME CONTAINING
        Restaurant restaurantCheck = restaurantDao.findAllByZipcodeAndNameContaining(restZipcode, restName);

        //Checking if lists exists
        if(listRes.getRestaurants() == null) {
            Set<Restaurant> startList = new HashSet<>();
            listRes.setRestaurants(startList);
        }

        if(restaurantToBeSaved.getLists() == null){
            Set<ListRestaurant> startListRest = new HashSet<>();
            restaurantToBeSaved.setLists(startListRest);
        }

        if (restaurantCheck == null){
            Set<RestaurantTag> emptyList = new HashSet<>();
            if(restaurantToBeSaved.getTags() != null){
                for(RestaurantTag tag : restaurantToBeSaved.getTags()) {
                    RestaurantTag apiTag = tagDao.findByName(tag.getName());
                    if (apiTag == null){
                        RestaurantTag newTag = new RestaurantTag(tag.getName());
                        emptyList.add(tagDao.save(newTag));
                    }else{
                        emptyList.add(apiTag);
                    }
                }
                restaurantToBeSaved.setTags(emptyList);
            }

            listRes.addRestaurantToList(restaurantToBeSaved); //this is working now
            restaurantDao.save(restaurantToBeSaved);
        } else {
            System.out.println("restaurantCheck.getName() = " + restaurantCheck.getName());
            listRes.addRestaurantToList(restaurantCheck); //this is also working now
            restaurantDao.save(restaurantCheck);
        }

        listDao.save(listRes);
        return listRes.getRestaurants();

    }

    @CrossOrigin
    @GetMapping("restaurant/show/{id}")
    Restaurant restShow (@PathVariable long id){
        return restaurantDao.findById(id);
    }

}
