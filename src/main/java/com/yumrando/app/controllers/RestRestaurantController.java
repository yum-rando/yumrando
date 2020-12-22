package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
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

    public RestRestaurantController(RestaurantRepository restaurantDao, ListRestaurantRepository listDao, UserRepository userDao) {
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
        this.userDao = userDao;
    }

//    @CrossOrigin
//    @PostMapping("restaurants/lists/{id}")
//    ListRestaurant restaurants (@RequestBody Restaurant restaurantToBeSave, @PathVariable long id){
//
//
//        //listDao.setId(id);
//        //ListRestaurant listRes = listDao.findById(id);
//        ListRestaurant listRes = listDao.findById(id);
//        //ListRestaurant listRes = listDao.findById(id);
//        //ListRestaurant listRes = listDao.findById(id);
//
//
//
//        System.out.println(listRes.getName());
//        System.out.println(restaurantToBeSave.getName());
//
//        System.out.println("listRes.getRestaurants() = " + listRes.getRestaurants());
//        System.out.println("restaurantToBeSave.getLists() = " + restaurantToBeSave.getLists());
//
//        //Establishing the Many-to-Many Relationship
//        //listRes.getRestaurants().add(restaurantToBeSave);
//
//        if (restaurantToBeSave.getLists() == null) {
//
//        }
//        restaurantToBeSave.getLists().add(listRes);
//        System.out.println(restaurantToBeSave.getLists());
//        listRes.getRestaurants().add(restaurantToBeSave);
//        System.out.println(listRes.getRestaurants());
//        restaurantDao.save(restaurantToBeSave);
//
//        List <Restaurant> restaurants = listRes.getRestaurants();
//        for (Restaurant res : restaurants) {
//            System.out.println(res.getName());
//        }
//
//
//        Restaurant rest = restaurantDao.save(restaurantToBeSave);
//        restaurants.add(rest);
//        listRes.setRestaurants(restaurants);
//
//        return listDao.save(listRes);
//    }

    @CrossOrigin
    @PostMapping("restaurants/lists/{id}")
    Set<Restaurant> restaurants (@RequestBody Restaurant restaurantToBeSaved, @PathVariable long id){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listDao.findAllByUser(userDb);
        //Need to retrieve the id from the list
        ListRestaurant listRes = listDao.findById(id);
        //Need to check the criteria for the restaurant
        String name = restaurantToBeSaved.getName();
        String apiId = restaurantToBeSaved.getApiId();

        //Set<Restaurant> dbRestToBeSavedCheck = restaurantDao.findAllByNameOrApiId(name, apiId);
        //Set<Restaurant> dbRestaurants = restaurantDao.findAllByLists(listRes);
        List<Restaurant> dbRestaurants = restaurantDao.findAll();

        //Need to establish the Many-To-Many relationship between restaurants and lists, however, 1st I need to verify if there are lists for both
        if(listRes.getRestaurants() == null){
            Set<Restaurant> startList = new HashSet<>();
            //Need to check if the restaurant to be save is already in the table
            //If yes,
            //Check if the restaurant is already the system by it’s name, address, zip code, or ID, if not, then add it to the restaurant table and add it to the list,
            // if it is, then do not add it to the restaurant table since it’s already there and add it to the listRestaurant table with the list the user decided
            startList.add(restaurantToBeSaved);
            listRes.setRestaurants(startList);
        } else {
            listRes.getRestaurants().add(restaurantToBeSaved);
            //listRes.addRestaurantToList(restaurantToBeSaved); // --> This does the operation for us with adding it to both sides of the many-to-many table
        }

        if(restaurantToBeSaved.getLists() == null){
            Set<ListRestaurant> startListRest = new HashSet<>();
            startListRest.add(listRes);
            restaurantToBeSaved.setLists(startListRest);
        } else {
            restaurantToBeSaved.getLists().add(listRes);
            //restaurantToBeSaved.addListToRestaurant(listRes); // --> This does the operation for us with adding it to both sides of the many-to-many table
        }

        //Applying the Many-To-Many Relationship
        //listRes.getRestaurants().add(restaurantToBeSaved);
        //restaurantToBeSaved.getLists().add(listRes);


        //Not done with below, will need to check if the apiId is null, as well as if it is the same as what is in the restaurant table
        for (Restaurant res : dbRestaurants) {
            if (apiId != null) {
                if (apiId.equals(res.getApiId()) || name.equalsIgnoreCase(res.getName())){
                    System.out.println("Testing the either the APIID or the NAME is the same");
                    listRes.addRestaurantToList(restaurantToBeSaved);
                } else {
                    listRes.addRestaurantToList(restaurantToBeSaved);
                    restaurantDao.save(restaurantToBeSaved);
                }
            } else {
                if (name.equalsIgnoreCase(res.getName())) {
                    System.out.println("Testing the either the APIID or the NAME is the same");
                    listRes.addRestaurantToList(restaurantToBeSaved);
                } else {
                    listRes.addRestaurantToList(restaurantToBeSaved);
                    restaurantDao.save(restaurantToBeSaved);
                }
            }
        }

        //This is where it is actually saving to the the restaurant table
        //restaurantDao.save(restaurantToBeSaved);

        for (Restaurant res : listRes.getRestaurants()) {
            System.out.println("res.getName() = " + res.getName());
        }

        //System.out.println(listRes.getRestaurants());

        return listRes.getRestaurants();

    }

}
