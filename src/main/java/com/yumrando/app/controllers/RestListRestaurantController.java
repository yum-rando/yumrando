package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.ListRestaurantNameOnly;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

@RestController
public class RestListRestaurantController {

    private UserRepository userDao;
    private ListRestaurantRepository listDao;

    public RestListRestaurantController(ListRestaurantRepository listDao, UserRepository userDao) {
        this.listDao = listDao;
        this.userDao = userDao;
    }
    @CrossOrigin
    @GetMapping("index/data")
    ListRestaurant one() { return listDao.findById(1);}



    //Add List -->Make sure no duplicate names
    @PostMapping("restaurants/lists/create")
   ListRestaurant newList(@RequestBody ListRestaurant list){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (ListRestaurant listItem : listDao.findAllByUser(userDb)) {
            if ((listItem.getName().equalsIgnoreCase(list.getName())) || list.getName().isEmpty()){
                return list;
            }
        }
        list.setUser(userDb);
        return listDao.save(list);
    }

    @CrossOrigin
    @PatchMapping("lists/{listId}/edit")
    public ListRestaurant editListObject(
            @RequestBody ListRestaurant listToBeUpdated,
            @PathVariable long listId
    ){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (ListRestaurant listItem : listDao.findAllByUser(userDb)) {
            if (listItem.getName().equalsIgnoreCase(listToBeUpdated.getName())){
                return listToBeUpdated;
            }
        }
        //Make sure that the listToBeUpdated is from the user and has the same id of the list so it can be updated with its name
        listToBeUpdated.setUser(userDb);
        listToBeUpdated.setId(listId);
        return listDao.save(listToBeUpdated);
    }
}
