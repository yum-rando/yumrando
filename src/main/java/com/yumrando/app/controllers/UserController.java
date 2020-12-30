package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
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
import java.text.SimpleDateFormat;
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
            return "redirect:/login"; // If password equals confirmPassword redirect to index
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = user.getId();
        List<ListRestaurant> listings = listDao.findAllByUserId(userId);
        model.addAttribute("lists", listings);
        return "user/profile";
    }

    @GetMapping("profile/{username}")
    public String showProfileSpecificUser(@PathVariable String username, Model vModel){
        vModel.addAttribute("userInfo", userDao.findByUsername(username));
        return "/user/profile";
    }

    @PutMapping("profile/{username}")
    public String editProfileBtn(@ModelAttribute User userToBeUpdated){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userToBeUpdated.setFirstName(userDb.getFirstName());
        userToBeUpdated.setLastName(userDb.getLastName());
        userToBeUpdated.setEmail(userDb.getEmail());
        userToBeUpdated.setPhoneNumber(userDb.getPhoneNumber());
        userToBeUpdated.setZipcode(userDb.getZipcode());
        userDao.save(userToBeUpdated);
        return "redirect:/profile/" + userToBeUpdated.getUsername();
    }

    //This is for the REVIEW CONTROLLER
    //UPDATING THE DATE IN THE SYSTEM --> MADE IT A STRING INSTEAD OF A DATE SINCE IT WAS MESSING UP WITH THE HIBERNATE
//    public void updateReviewTime(Review review){
//        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        review.setUser(userDb);
//        Date now = new Date();
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//        String mysqlUpdateDate = formatter.format(now);
//        review.setUpdateTime(mysqlUpdateDate);
//        reviewDao.save(review);
//    }


    @PostMapping("/logout")
    @ResponseBody
    public String executeLogout() {
        return "redirect:/index";
    }



}
