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
        //All restaurants in DB
        List<Restaurant> dbRestaurants = restaurantDao.findAll();

        //Set<Restaurant> dbRestToBeSavedCheck = restaurantDao.findAllByNameOrApiId(name, apiId);
        //Set<Restaurant> dbRestaurants = restaurantDao.findAllByLists(listRes);


        //Need to establish the Many-To-Many relationship between restaurants and lists, however, 1st I need to verify if there are lists for both
        if(listRes.getRestaurants() == null){
            Set<Restaurant> startList = new HashSet<>();
            //Need to check if the restaurant to be save is already in the table
            //If yes,
            //Check if the restaurant is already the system by it’s name or apiId, if not, then add it to the restaurant table and add it to the list,
            // if it is, then do not add it to the restaurant table since it’s already there and add it to the listRestaurant table with the list the user decided

            //Work on this tomorrow Tuesday: 12/22/2020
            //Need to verify if restaurantToBeSaved is already in the DB, if so, then just add it to the list and update the listDao
            //If not in DB, then need to add it to the list and the DB via the restaurantDao and still updating the listDao


            startList.add(restaurantToBeSaved);
            listRes.setRestaurants(startList);
        } else {
            //listRes.getRestaurants().add(restaurantToBeSaved);
            //listRes.addRestaurantToList(restaurantToBeSaved); // --> This does the operation for us with adding it to both sides of the many-to-many table
//            for (Restaurant res : dbRestaurants) {
//                if (apiId != null && apiId.equals(res.getApiId())){
//                    System.out.println("Testing if the API CREDENTIALS ARE WORKING - LIST");
//                    System.out.println("listRes = " + listRes.getName());
//                    System.out.println("listRes.getRestaurants() = " + listRes.getRestaurants());
//                    System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
//                    listRes.getRestaurants().add(restaurantToBeSaved);
//                    //restaurantToBeSaved.addListToRestaurant(listRes);
//                    //listRes.addRestaurantToList(restaurantToBeSaved);
//                }else if (name.equalsIgnoreCase(res.getName())){
//                    System.out.println("Testing if the NAME CREDENTIALS ARE WORKING - LIST");
//                    listRes.getRestaurants().add(restaurantToBeSaved);
//                    //listRes.addRestaurantToList(restaurantToBeSaved);
//                    //restaurantToBeSaved.addListToRestaurant(listRes);
//                    //listRes.addRestaurantToList(restaurantToBeSaved);
//                } else {
//                    listRes.getRestaurants().add(restaurantToBeSaved);
//                }
//            }

            for (Restaurant res : dbRestaurants) {
                if ((apiId != null && !apiId.equals(res.getApiId())) || !name.equalsIgnoreCase(res.getName())){
                    System.out.println("Testing if the API & NAME CREDENTIALS ARE WORKING - LIST");
                    System.out.println("listRes = " + listRes.getName());
                    System.out.println("listRes.getRestaurants() = " + listRes.getRestaurants());
                    System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                    listRes.getRestaurants().add(restaurantToBeSaved);
                    restaurantDao.save(restaurantToBeSaved);
                    //restaurantToBeSaved.addListToRestaurant(listRes);
                    //listRes.addRestaurantToList(restaurantToBeSaved);
                } else {
                    listRes.getRestaurants().add(restaurantToBeSaved);
                }
            }



        }


        if(restaurantToBeSaved.getLists() == null){
            Set<ListRestaurant> startListRest = new HashSet<>();
            startListRest.add(listRes);
            restaurantToBeSaved.setLists(startListRest);
        } else {
            //restaurantToBeSaved.getLists().add(listRes);
            //restaurantToBeSaved.addListToRestaurant(listRes); // --> This does the operation for us with adding it to both sides of the many-to-many table
            for (Restaurant res : dbRestaurants) {
                if (apiId != null && apiId.equals(res.getApiId())){
                    System.out.println("Testing if the API CREDENTIALS ARE WORKING - RES");
                    //restaurantToBeSaved.addListToRestaurant(listRes);
                    restaurantToBeSaved.getLists().add(listRes);
                }else if (name.equalsIgnoreCase(res.getName())){
                    System.out.println("Testing if the NAME CREDENTIALS ARE WORKING - RES");
                    //restaurantToBeSaved.addListToRestaurant(listRes);
                    restaurantToBeSaved.getLists().add(listRes);
                } else {
                    //restaurantToBeSaved.addListToRestaurant(listRes);
                    restaurantToBeSaved.getLists().add(listRes);
                }
            }
        }

        //Applying the Many-To-Many Relationship
        //listRes.getRestaurants().add(restaurantToBeSaved);
        //restaurantToBeSaved.getLists().add(listRes);


        //Not done with below, will need to check if the apiId is null, as well as if it is the same as what is in the restaurant table
//        for (Restaurant res : dbRestaurants) {
//            if (apiId != null && apiId.equals(res.getApiId())){
//                System.out.println("Testing if the API CREDENTIALS ARE WORKING");
//                listRes.addRestaurantToList(restaurantToBeSaved);
//            }else if (name.equalsIgnoreCase(res.getName())){
//                System.out.println("Testing if the NAME CREDENTIALS ARE WORKING");
//                listRes.addRestaurantToList(restaurantToBeSaved);
//            }
//        }





//        for (Restaurant res : dbRestaurants) {
//            if (apiId != null) {
//                if (apiId.equals(res.getApiId()) || name.equalsIgnoreCase(res.getName())){
//                    System.out.println("Testing the either the APIID or the NAME is the same");
//                    listRes.addRestaurantToList(restaurantToBeSaved);
//                } else {
//                    listRes.addRestaurantToList(restaurantToBeSaved);
//                    restaurantDao.save(restaurantToBeSaved);
//                }
//            } else {
//                if (name.equalsIgnoreCase(res.getName())) {
//                    System.out.println("Testing the either the APIID or the NAME is the same");
//                    listRes.addRestaurantToList(restaurantToBeSaved);
//                } else {
//                    listRes.addRestaurantToList(restaurantToBeSaved);
//                    restaurantDao.save(restaurantToBeSaved);
//                }
//            }
//        }


        for (Restaurant res : dbRestaurants) {
            if ((apiId != null && !apiId.equals(res.getApiId())) || !name.equalsIgnoreCase(res.getName())){
                System.out.println("Testing if the API & NAME CREDENTIALS ARE WORKING - LIST");
                System.out.println("listRes = " + listRes.getName());
                System.out.println("listRes.getRestaurants() = " + listRes.getRestaurants());
                System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                listRes.getRestaurants().add(restaurantToBeSaved);
                restaurantDao.save(restaurantToBeSaved);
                //restaurantToBeSaved.addListToRestaurant(listRes);
                //listRes.addRestaurantToList(restaurantToBeSaved);
            } else {
                listRes.getRestaurants().add(restaurantToBeSaved);
            }
        }

        //This is where it is actually saving to the the restaurant table
        //Need this to save a new restaurant not an already in table restaurant
        //restaurantDao.save(restaurantToBeSaved);

        //Add to my list since it is being updated by the new restaurant
        listDao.save(listRes);

        for (Restaurant res : listRes.getRestaurants()) {
            System.out.println("res.getName() = " + res.getName());
        }

        //System.out.println(listRes.getRestaurants());

        return listRes.getRestaurants();

    }

    private void checkRestaurantToBeSaved (Set<Restaurant> restaurants, Restaurant resToBeSaved, ListRestaurant list){
        String apiId = resToBeSaved.getApiId();
        String resName = resToBeSaved.getName();
        for (Restaurant res : restaurants) {
            if ((apiId != null && !apiId.equals(res.getApiId())) || !resName.equalsIgnoreCase(res.getName())){
                System.out.println("Testing if the API & NAME CREDENTIALS ARE WORKING - LIST");
                System.out.println("listRes = " + list.getName());
                System.out.println("listRes.getRestaurants() = " + list.getRestaurants());
                System.out.println("restaurantToBeSaved = " + resToBeSaved.getName());
                list.getRestaurants().add(resToBeSaved);
                restaurantDao.save(resToBeSaved);
                //restaurantToBeSaved.addListToRestaurant(listRes);
                //listRes.addRestaurantToList(restaurantToBeSaved);
            } else {
                list.getRestaurants().add(resToBeSaved);
            }
        }
    }

}
