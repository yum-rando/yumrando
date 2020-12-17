package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestRestaurantController {
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;

    public RestRestaurantController(RestaurantRepository restaurantDao, ListRestaurantRepository listDao) {
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
    }

    @CrossOrigin
    @PostMapping("restaurants/lists/{id}")
    ListRestaurant restaurants (@RequestBody Restaurant restaurantToBeSave, @PathVariable long id){
        //listDao.setId(id);
        ListRestaurant listRes = listDao.findById(id);
        List <Restaurant> restaurants = listRes.getRestaurants();
        for (Restaurant res : restaurants) {
            System.out.println(res.getName());
        }
        Restaurant rest = restaurantDao.save(restaurantToBeSave);
        restaurants.add(rest);
        listRes.setRestaurants(restaurants);

        return listDao.save(listRes);
    }
}
