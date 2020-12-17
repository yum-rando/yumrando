package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    //Add List -->Make sure no duplicate names
    @PostMapping("restaurants/lists/create")
    private String createListOfRestaurants(@ModelAttribute ListRestaurant listToBeSaved){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String listName = listToBeSaved.getName().trim();
        List<ListRestaurant> listR = listDao.findAll();
        for (ListRestaurant list : listR) {
            if (list.getName().equalsIgnoreCase(listName)){
                //inform user this can't be done; Already a list of this name, try again with another name
                System.out.println("This will not work");
            }
        }
        listToBeSaved.setUser(userDb);
        ListRestaurant dbListRestaurant = listDao.save(listToBeSaved);
        return "redirect:/restaurants/lists/" + dbListRestaurant.getName();
        //return "user/profile";
    }

    @PostMapping("restaurants/lists/create/test")
    private String createList(@ModelAttribute ListRestaurant listToBeSaved){
        List<Restaurant> restaurantList = listToBeSaved.getRestaurants();
        //Not done yet and still working on this
        return "index";
    }

    //Update List
    @PostMapping("restaurants/lists/edit")
    private String editListRestaurant(@ModelAttribute ListRestaurant listToBeUpdated){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        listToBeUpdated.setUser(userDb);
        listDao.save(listToBeUpdated);

        //redirect to the specific ListRestaurants page
        return "redirect:/restaurants/lists/" + listToBeUpdated.getName();
    }

    //Delete List by the ListRestaurant Id
    @PostMapping("restaurants/lists/{id}/delete")
    private String deleteListById(@PathVariable long id){
        System.out.println("Will this run?");
        listDao.deleteById(id);
        return "redirect:/index";
    }

    //Delete List by the ListRestaurant Name
    @PostMapping("restaurant/lists/{listName}")
    private String deleteListByListName(@PathVariable String listName){
        System.out.println("Will this run?");
        listDao.deleteByName(listName);
        return "index";
    }

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
