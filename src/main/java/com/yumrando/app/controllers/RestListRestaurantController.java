package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

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
            if (listItem.getName().equalsIgnoreCase(list.getName())){
                return list;
            }
        }
        list.setUser(userDb);
        return listDao.save(list);
//        return "redirect:/restaurants/lists/" + dbListRestaurant.getName();

    }
}