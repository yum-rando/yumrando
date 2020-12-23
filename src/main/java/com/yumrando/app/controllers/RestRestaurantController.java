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

    @CrossOrigin
    @PostMapping("restaurants/lists/{id}")
    Set<Restaurant> restaurants (@RequestBody Restaurant restaurantToBeSaved, @PathVariable long id){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listDao.findAllByUser(userDb);
        //Need to retrieve the id from the list
        //ListRestaurant listRes = listDao.findById(id);
        ListRestaurant listRes = listDao.findAllByUserAndId(userDb, id);
        //Find all list tied to the restaurantToBeSaved
        //Set<ListRestaurant> restToBeSavedLists = listDao.findAllByRestaurants(restaurantToBeSaved);
        //restaurantToBeSaved.setLists(restToBeSavedLists);
        //restaurantToBeSaved.setLists(listDao.findAllByRestaurants(restaurantToBeSaved));
        //restaurantDao.save(restaurantToBeSaved);
        //System.out.println("restToBeSavedLists = " + restToBeSavedLists);
        //Need to check the criteria for the restaurant
        String name = restaurantToBeSaved.getName();
        String apiId = restaurantToBeSaved.getApiId();
        Set<ListRestaurant> resList = restaurantToBeSaved.getLists();
        //System.out.println("resList = " + resList); // null
        //resList = listDao.findAllByRestaurants(restaurantToBeSaved); //brings an exception error
        //System.out.println("resList = " + resList);
        //Verification of RestaurantToBeSaved
        Set<Restaurant> restaurantsToBeSavedDBCheck = restaurantDao.findAllByApiIdOrName(apiId, name);
        // Running a query method to see if there is an APIID or Restaurant Name already in the database and put it in this set --> This is working
        //Possible bug --> different api ids or no api id with the same name --> need to also check via the address and zipcode for further verification if it is in the restaurant table
        if (restaurantsToBeSavedDBCheck.isEmpty()){
            //If this is empty, it means that the query couldn't find the restaurant in the DB so it needs to add it to the list AND add it to the restaurant table
            System.out.println("this RESTAURANT is NOT in DB = " + true);
            System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
            //restaurantToBeSaved.setLists(listDao.findAllByRestaurants(restaurantToBeSaved));
            System.out.println("restaurantToBeSaved.getLists() = " + restaurantToBeSaved.getLists()); //coming out null
            listRes.getRestaurants().add(restaurantToBeSaved);
            //restaurantToBeSaved.addListToRestaurant(listRes);
            //listRes.addRestaurantToList(restaurantToBeSaved);
            restaurantDao.save(restaurantToBeSaved);
        } else {
            //query found something in the DB so it just needs to add it to the list ONLY
            //restaurantToBeSaved.setLists(listDao.findAllByRestaurants(restaurantToBeSaved));
            System.out.println("restaurantToBeSaved.getLists() = " + restaurantToBeSaved.getLists());
            System.out.println("this RESTAURANT IS in DB = " + true);
            System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
            //restaurantToBeSaved.addListToRestaurant(listRes);
            //listRes.addRestaurantToList(restaurantToBeSaved);

            listRes.getRestaurants().add(restaurantToBeSaved);
        }


        //Need to establish the Many-To-Many relationship between restaurants and lists, however, 1st I need to verify if there are lists for both
        if(listRes.getRestaurants() == null){
            Set<Restaurant> startList = new HashSet<>();
            //Need to check if the restaurant to be save is already in the table
            //If yes,
            //Check if the restaurant is already the system by it’s name or apiId, if not, then add it to the restaurant table and add it to the list,
            // if it is, then do not add it to the restaurant table since it’s already there and add it to the listRestaurant table with the list the user decided

            //Work on this tomorrow Tuesday: 12/22/2020
            //Need to verify if restaurantToBeSaved is already in the DB
            // if so, then just add it to the list and update the listDao
            //If not in DB, then need to add it to the list and the DB via the restaurantDao and still updating the listDao


            startList.add(restaurantToBeSaved);
            listRes.setRestaurants(startList);
        } else {
            if (restaurantsToBeSavedDBCheck.isEmpty()){
                listRes.getRestaurants().add(restaurantToBeSaved);
                //If this is empty, it means that the query couldn't find the restaurant in the DB so it needs to add it to the list AND add it to the restaurant table
                System.out.println("this RESTAURANT is NOT in DB = " + true);
                System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                //listRes.getRestaurants().add(restaurantToBeSaved);
                //listRes.addRestaurantToList(restaurantToBeSaved);
                restaurantDao.save(restaurantToBeSaved);
            } else {
                //query found something in the DB so it just needs to add it to the list ONLY
                //listRes.addRestaurantToList(restaurantToBeSaved);
                System.out.println("this RESTAURANT IS in DB = " + true);
                System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                listRes.getRestaurants().add(restaurantToBeSaved);
            }



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
            }


        if(restaurantToBeSaved.getLists() == null){
            Set<ListRestaurant> startListRest = new HashSet<>();
            startListRest.add(listRes);
            restaurantToBeSaved.setLists(startListRest);
        } else {
            if (restaurantsToBeSavedDBCheck.isEmpty()){
                restaurantToBeSaved.getLists().add(listRes);
                //If this is empty, it means that the query couldn't find the restaurant in the DB so it needs to add it to the list AND add it to the restaurant table
                System.out.println("this RESTAURANT is NOT in DB = " + true);
                System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                restaurantToBeSaved.getLists().add(listRes);
                //listRes.addRestaurantToList(restaurantToBeSaved);
                restaurantDao.save(restaurantToBeSaved);
            } else {
                //query found something in the DB so it just needs to add it to the list ONLY
                //listRes.addRestaurantToList(restaurantToBeSaved);
                System.out.println("this RESTAURANT IS in DB = " + true);
                System.out.println("restaurantToBeSaved = " + restaurantToBeSaved.getName());
                restaurantToBeSaved.getLists().add(listRes);
            }


            //restaurantToBeSaved.addListToRestaurant(listRes); // --> This does the operation for us with adding it to both sides of the many-to-many table
//            for (Restaurant res : dbRestaurants) {
//                if (apiId != null && apiId.equals(res.getApiId())){
//                    System.out.println("Testing if the API CREDENTIALS ARE WORKING - RES");
//                    //restaurantToBeSaved.addListToRestaurant(listRes);
//                    restaurantToBeSaved.getLists().add(listRes);
//                }else if (name.equalsIgnoreCase(res.getName())){
//                    System.out.println("Testing if the NAME CREDENTIALS ARE WORKING - RES");
//                    //restaurantToBeSaved.addListToRestaurant(listRes);
//                    restaurantToBeSaved.getLists().add(listRes);
//                } else {
//                    //restaurantToBeSaved.addListToRestaurant(listRes);
//                    restaurantToBeSaved.getLists().add(listRes);
//                }
//            }
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


    //Revisit this tomorrow this is not right
    private boolean checkRestaurantToBeSaved (Set<Restaurant> restaurants, Restaurant resToBeSaved, ListRestaurant list){
        String apiId = resToBeSaved.getApiId();
        String resName = resToBeSaved.getName();
        boolean check = false;
        for (Restaurant res : restaurants) {
            check = (apiId != null && !apiId.equals(res.getApiId())) || !resName.equalsIgnoreCase(res.getName()) ? false : true;
//            if ((apiId != null && !apiId.equals(res.getApiId())) || !resName.equalsIgnoreCase(res.getName())){
//                //System.out.println("Testing if the API & NAME CREDENTIALS ARE WORKING - LIST");
//                //System.out.println("listRes = " + list.getName());
//                //System.out.println("listRes.getRestaurants() = " + list.getRestaurants());
//                //System.out.println("restaurantToBeSaved = " + resToBeSaved.getName());
//                //list.getRestaurants().add(resToBeSaved);
//                //restaurantDao.save(resToBeSaved);
//                //restaurantToBeSaved.addListToRestaurant(listRes);
//                //listRes.addRestaurantToList(restaurantToBeSaved);
//                return false;
//            } else {
//                list.getRestaurants().add(resToBeSaved);
//            }
        }
        return check;
    }

}
