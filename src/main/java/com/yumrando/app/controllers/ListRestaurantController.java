package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
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

    @GetMapping("restaurants/{username}/{listName}")
    private String viewSpecificRestaurantList(@PathVariable String username, @PathVariable String listName, Model vModel){
        User user = userDao.findByUsername(username);
        ListRestaurant specificList = listDao.findAllByUserAndName(user, listName);
        vModel.addAttribute("specificList", specificList);
        return "user/profile";
    }

    @PostMapping("restaurants/lists/create/test")
    private String createList(@ModelAttribute ListRestaurant listToBeSaved){
        Set<Restaurant> restaurantList = listToBeSaved.getRestaurants();
        //Not done yet and still working on this
        return "index";
    }

    //Take what is is from the UserController and input it here since it is impacting the list information

    //UPDATING the List Name Here
    //@PatchMapping("/{username}/list/{listId}/edit")
    @PatchMapping("/lists/{listId}/edit")
    public String editListName(
            //@PathVariable String username,
            @PathVariable long listId,
            @ModelAttribute ListRestaurant list){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant listDb = listDao.findAllByUserAndId(userDb, listId);
        listDb.setUser(userDb);
        //list.setUser(userDb);
        listDao.save(listDb);
        //listDao.save(list);
        return "redirect:/user/profile";
    }

    //DELETING the List from the USER here
    @PostMapping("/delete/lists/{listId}")
    public String deleteListFromUser(@PathVariable long listId){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant list = listDao.findAllByUserAndId(userDb, listId);
        userDb.removeListFromUser(list);
        userDao.save(userDb);
        return "redirect:/user/profile";
    }

    //Update List -->another method will test this later
    @PostMapping("restaurants/lists/edit")
    private String editListRestaurant(@ModelAttribute ListRestaurant listToBeUpdated){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listToBeUpdated.setUser(userDb);
        listDao.save(listToBeUpdated);

        //redirect to the specific ListRestaurants page
        return "redirect:/restaurants/lists/" + listToBeUpdated.getName();
    }

    //Delete List by the ListRestaurant Id
//    @PostMapping("restaurants/lists/{id}/delete")
//    private String deleteListById(@PathVariable long id){
//        System.out.println("Will this run?");
//        listDao.deleteById(id);
//        return "redirect:/index";
//    }

    //Delete List by the ListRestaurant Name
//    @PostMapping("restaurant/lists/{listName}")
//    private String deleteListByListName(@PathVariable String listName){
//        System.out.println("Will this run?");
//        listDao.deleteByName(listName);
//        return "index";
//    }

    //Delete List via the Model Attribute
    @PostMapping("restaurant/lists/delete")
    private String deleteListByModelAttr(@ModelAttribute ListRestaurant listToBeDeleted){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listToBeDeleted.setUser(userDb);
        listDao.deleteByName(listToBeDeleted.getName());
        listDao.deleteById(listToBeDeleted.getId());

        return "index";
    }


    //Different ListName Change --> Possibly needed
}
