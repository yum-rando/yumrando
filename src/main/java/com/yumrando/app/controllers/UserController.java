package com.yumrando.app.controllers;

import com.yumrando.app.models.*;
import com.yumrando.app.repos.ListFriendsRepository;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import com.yumrando.app.repos.Users;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {
    private Users users;
    private PasswordEncoder passwordEncoder;
    private final UserRepository userDao;
    private final ListRestaurantRepository listDao;
    private final ListFriendsRepository friendDao;
    private final ReviewRepository reviewDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, Users users, ListRestaurantRepository listDao, ReviewRepository reviewDao, ListFriendsRepository friendDao){

        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
        this.listDao = listDao;
        this.friendDao = friendDao;
        this.reviewDao = reviewDao;

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
    public String showUsersLists(Model vModel, Principal user, @PathVariable long id) {
        if (user != null) {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //Chosen List should not be part of the Other list options
            ListRestaurant chosenList = listDao.findAllByUserAndId(userDb, id);
            //This is the Other Lists
            List<ListRestaurant> nonChosenList = new ArrayList<>();
            //Get all of the List from the User
            List<ListRestaurant> lists = listDao.findAllByUser(userDb);

            for (ListRestaurant list : lists) {
                if (list.getId() != id) {
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
    public String saveUser(
            @Valid User user,
            Errors validation,
            @RequestParam(name = "confirmPassword") String checkPassword,
            Model model) {
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("user", user);
            System.out.println(validation.getAllErrors());
            return "user/register";
        } else if (user.getPassword().equals(checkPassword)) {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            users.save(user);
            return "redirect:/"; // If password equals confirmPassword redirect to index)
        }
        return "user/register";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = user.getId();
        List<ListRestaurant> listings = listDao.findAllByUserId(userId);
        List<Review> reviews = reviewDao.findAllByUserOrderByUpdateTimeDesc(user);
        model.addAttribute("lists", listings);
        model.addAttribute("userInfo", userDao.findById(userId));
        model.addAttribute("friends", friendDao.findAllByUserId(user.getId()));
        model.addAttribute("requests", friendDao.findAllByFriendId(user.getId()));
        //Need to show the info for the reviews or restaurants in the the descending order for specific user
        model.addAttribute("history", reviews);
        return "user/profile";
    }

    //Would have liked to used a Patch but used a Post instead due to the html form not accepting a Patch method
    @PostMapping("/profile")
    public String editProfileBtn(@ModelAttribute User userToBeUpdated) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userToBeUpdated.setId(userDb.getId()); //Makes sure the userToBeUpdated is the same as the logged in user
        userToBeUpdated.setPassword(userDb.getPassword()); //Needed this since the password can't be null in a post
        userToBeUpdated.setUsername(userDb.getUsername()); //Needed this since the username can't be null in a post
        userDao.save(userToBeUpdated);
        return "redirect:/profile";
    }


    @PostMapping("/profile/friend/accept/{id}")
    public String acceptFriend (@PathVariable long id){
        FriendList updateFriend = friendDao.findById(id);
        updateFriend.setConfirmation(true);
        friendDao.save(updateFriend);
        return "redirect:/profile";
    }

    @PostMapping("/profile/friend/delete/{id}")
    public String deleteFriend (@PathVariable long id){
        friendDao.deleteById(id);
        return "redirect:/profile";
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


}
