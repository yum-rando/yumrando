package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.UserRepository;
import com.yumrando.app.repos.Users;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class UserController {
    private Users users;
    private PasswordEncoder passwordEncoder;
    private final UserRepository userDao;
    private final ListRestaurantRepository listDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, Users users, ListRestaurantRepository listDao){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
        this.listDao = listDao;
    }

    @GetMapping("/")
    public String showIndexPage(Model model, Principal user) {
        if (user != null) {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("lists", listDao.findAllByUser(userDb));
        }
        return "index";
    }

    //User will go to a page with the list options
    @GetMapping("/{id}")
    public String showUsersLists(Model vModel, Principal user, @PathVariable long id){
        if (user != null) {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //Chosen List should not be part of the Other list options
            ListRestaurant chosenList = listDao.findAllByUserAndId(userDb, id);
            //This is the Other Lists
            List<ListRestaurant> nonChosenList = new ArrayList<>();
            //Get all of the List from the User
            List<ListRestaurant> lists = listDao.findAllByUser(userDb);

            for (ListRestaurant list : lists) {
                if (list.getId() != id){
                    nonChosenList.add(list);
                }
            }
            //Chosen List Object to the View
            vModel.addAttribute("chosenList", chosenList);
            //Other list should not include the chosen list
            vModel.addAttribute("lists", nonChosenList);
        }
        return "index";
    }

//    //User Can Delete a Restaurant from the ListOfRestaurants
//    @PostMapping("/delete/{listId}/{restaurantIdToBeDeleted}")
//    public String deleteRestaurantFromList(@PathVariable long listId, @PathVariable long restaurantIdToBeDeleted){
//        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        //ListRestaurant list = listDao.getOne(listId);
//        ListRestaurant list = listDao.findAllByUserAndId(userDb, listId);
//        Set<Restaurant> restaurants = list.getRestaurants();
//        Set<Restaurant> newRestaurantList = new HashSet<>();
//        //restaurants.removeIf(restaurant -> restaurant.getId() == restaurantIdToBeDeleted);
//        for (Restaurant res : restaurants) {
//            if (res.getId() != restaurantIdToBeDeleted){
//                //System.out.println("res.getId() To be compared to restaurantId = " + res.getId());
//                //System.out.println("restaurantIdToBeDeleted = " + restaurantIdToBeDeleted);
//                newRestaurantList.add(res);
//            }
//        }
//
//        for (Restaurant res : newRestaurantList) {
//            System.out.println("res.getName() = " + res.getName());
//        }
//
//        list.setRestaurants(newRestaurantList);
//
//        //vModel.addAttribute("lists", list); --> didn't work
//
//        listDao.save(list);
//
//        return "redirect:/" + listId;
//    }




    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, @RequestParam (name = "confirmPassword") String checkPassword) {
        if (user.getPassword().equals(checkPassword)) {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            users.save(user);
            return "redirect:/index"; // If password equals confirmPassword redirect to index
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String executeLogout() {
        return "redirect:/index";
    }

}
