package com.yumrando.app.controllers;

import com.yumrando.app.models.*;
import com.yumrando.app.repos.*;
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
import java.util.stream.Collectors;

@Controller
public class UserController {
    private Users users;
    private PasswordEncoder passwordEncoder;
    private final UserRepository userDao;
    private final ListRestaurantRepository listDao;
    private final ListFriendsRepository friendDao;
    private final ReviewRepository reviewDao;
    private final TagRepository tagDao;
    private final RestaurantRepository restaurantDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, Users users, ListRestaurantRepository listDao, ReviewRepository reviewDao, ListFriendsRepository friendDao, TagRepository tagDao, RestaurantRepository restaurantDao){

        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
        this.listDao = listDao;
        this.friendDao = friendDao;
        this.reviewDao = reviewDao;
        this.tagDao = tagDao;
        this.restaurantDao = restaurantDao;
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

    @GetMapping("/{id}/filter")
    public String showUsersListsFilter(Model vModel, Principal user, @PathVariable long id) {
        if (user != null) {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //Chosen List should not be part of the Other list options
            ListRestaurant chosenList = listDao.findAllByUserAndId(userDb, id);
            Set<Restaurant> restList = new HashSet<>();
            ListRestaurant newList = new ListRestaurant(chosenList.getId(), chosenList.getName(), chosenList.getUser(), restList);
            Set<Restaurant> originalList = restaurantDao.findAllByLists(chosenList);
            List<RestaurantTag> userFavorites = tagDao.findAllByUsersId(userDb.getId());
            for(Restaurant rest: originalList){
                for(RestaurantTag tag : userFavorites){
                    if(restaurantDao.findByTagsAndId(tag, rest.getId()) != null){
                        newList.addRestaurantToList(rest);
                    }
                }
            }
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
            vModel.addAttribute("chosenList", newList);
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
            return "redirect:/login"; // If password equals confirmPassword redirect to index)
        }
        return "user/register";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = user.getId();
        List<ListRestaurant> listings = listDao.findAllByUserId(userId);
        List<Review> reviews = reviewDao.findTop5ByUserOrderByUpdateTimeDesc(user);
        List<RestaurantTag> restTag = tagDao.findAllByUsersId(userId);
        model.addAttribute("lists", listings);
        model.addAttribute("userInfo", userDao.findById(userId));
        model.addAttribute("friends", friendDao.findAllByUserId(user.getId()));
        model.addAttribute("requests", friendDao.findAllByFriendId(user.getId()));
        //Need to show the info for the reviews or restaurants in the the descending order for specific user
        model.addAttribute("history", reviews);
        model.addAttribute("faveTagList", restTag);

        return "user/profile";
    }

    //Would have liked to used a Patch but used a Post instead due to the html form not accepting a Patch method
    @PostMapping("/profile")
    public String editProfileBtn(@ModelAttribute User userToBeUpdated) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userToBeUpdated.setId(userDb.getId()); //Makes sure the userToBeUpdated is the same as the logged in user
        if (userToBeUpdated.getEmail().isEmpty()){
            userToBeUpdated.setEmail(null);
        }
        if(userToBeUpdated.getPhoneNumber().isEmpty()){
            userToBeUpdated.setPhoneNumber(null);
        }
        userToBeUpdated.setPassword(userDb.getPassword()); //Needed this since the password can't be null in a post
        userToBeUpdated.setUsername(userDb.getUsername()); //Needed this since the username can't be null in a post
        userDao.save(userToBeUpdated);
        return "redirect:/profile";
    }


    @PostMapping("/profile/friend/accept/{id}")

    public String acceptFriend(@PathVariable long id) {
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

    @GetMapping("/friend/{id}")
    public String viewFriend (@PathVariable long id, Model model){
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendList friendCheck = friendDao.findAllByUserIdAndFriendId(currUser.getId(), id);
        FriendList inverseCheck = friendDao.findAllByUserIdAndFriendId(id, currUser.getId());
        if(friendCheck != null || inverseCheck != null){
            if (friendCheck == null && inverseCheck.getConfirmation()){
                ListRestaurant chosenList = listDao.findFirstByUserId(id);
                if (chosenList == null){
                    return "redirect:/friend/" + id + "/list/0";
                } else {
                    return "redirect:/friend/" + id + "/list/" + chosenList.getId();
                }
            } else if (inverseCheck == null && friendCheck.getConfirmation()){
                ListRestaurant chosenList = listDao.findFirstByUserId(id);
                if (chosenList == null){
                    return "redirect:/friend/" + id + "/list/0";
                } else {
                    return "redirect:/friend/" + id + "/list/" + chosenList.getId();
                }

            }
        }
        return "redirect:/profile";
    }

    @GetMapping("/friend/{id}/list/{listId}")
    public String viewFriendSpecific (@PathVariable long id, @PathVariable long listId, Model model){
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendList friendCheck = friendDao.findAllByUserIdAndFriendId(currUser.getId(), id);
        FriendList inverseCheck = friendDao.findAllByUserIdAndFriendId(id, currUser.getId());
        if(friendCheck != null || inverseCheck != null){
            ListRestaurant chosenList = listDao.findById(listId);
            return getString(id, model, chosenList);
        }
        return "redirect:/profile";
    }

    private String getString(@PathVariable long id, Model model, ListRestaurant chosenList) {
        User friend = userDao.findById(id);
        List<ListRestaurant> lists = listDao.findAllByUserId(id);
        List<ListRestaurant> filteredList = lists.stream().filter(l -> l.getId() != chosenList.getId()).collect(Collectors.toList());
        model.addAttribute("chosenList", chosenList);
        model.addAttribute("friend", friend);
        model.addAttribute("lists", filteredList);
        return "user/friend";
    }
  
  @GetMapping("/about")
    public String about(Model model) {
        return "user/about";
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


    @GetMapping("/landing")
    public String landing(Model model) {
        return "landing";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }
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



