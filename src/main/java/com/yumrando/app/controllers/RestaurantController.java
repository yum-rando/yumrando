package com.yumrando.app.controllers;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class RestaurantController {

    private UserRepository userDao;
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;
    private ReviewRepository reviewDao;

    public RestaurantController(UserRepository userDao, RestaurantRepository restaurantDao, ListRestaurantRepository listDao, ReviewRepository reviewDao) {
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
        this.reviewDao = reviewDao;
    }

    //Reading/Showing all Restaurants
    @GetMapping("/restaurant")
    public String showRestaurants(Model vModel){
        vModel.addAttribute("restaurants", restaurantDao.findAll());
        return "index";
    }

    //Reading/Selecting 10 most recent Restaurant Choices
    @GetMapping("/restaurants/{username}/most-recent")
    public String showMostRecent(@PathVariable String username, Model vModel){
        System.out.println(username);
        User user = userDao.findByUsername(username);
        //Review review = reviewDao.findReviewByRestaurantId(1).getUser();

        //Need to review all the restaurants to sort out by the 10 most recent chosen
        //Used the stream method to further apply limit of 10 option to the list of restaurants
        //List<Restaurant> mostRecent = restaurantDao.findByOrderByChosenTimeDesc();

        List<Review> reviews = reviewDao.findAllByUser(user);

        //Send to the front
        vModel.addAttribute("recentReviews", reviews);


        //Issue --> How to tie this in with the specific user and their choices for restaurants
            //Might have to go thru the Review table since that is a direct connection between Users and Restaurants without a list

        return "user/test";
    }

    //Randomizer Button --> POST REQUEST
    //Front end will randomize the restaurant and send the final randomized choice to back end with an ID
    //That will then be search on the back end via that ID to find specific restaurant to update its chosen date
//    @PostMapping("/restaurant/randomizer/{id}")
//    public String randomizerBtn(@PathVariable long id){
////        //This is not needed since it will be done on the front end
////        List<Restaurant> restaurants = restaurantDao.findAll();
////        int randomizerIndex = new Random().nextInt(restaurants.size());
////        Restaurant randomizedRestaurant = restaurants.get(randomizerIndex);
//
//        //User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = reviewDao.findReviewByRestaurantId(id).getUser();
//        Restaurant restaurantChosen = restaurantDao.findById(id);
//        restaurantChosen.setChosenTime(new Date());
//
//        //New updated date save to the chosen randomized restaurant
//        restaurantDao.save(restaurantChosen);
//
//
//        //Formatting the current date to send to DB for update
//        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //Current Date
//        String currentDate = sdFormat.format(new Date());
//
//        //Sending the new updated time to the DB restaurant table as an object
//        //restaurantDao.save();
//
//        //vModel.addAttribute("randomRestaurant", randomizedRestaurant);
//
//        return "redirect:/user/profile";
//    }


    //Adding Restaurant //(Any user, especially if new restaurant) Double Check to make sure this restaurant isn't already in the database



    //Deleting Restaurant (Admin)

    @PostMapping("/delete/{listId}/{restaurantIdToBeDeleted}")
    public String deleteRestaurantFromList(@PathVariable long listId, @PathVariable long restaurantIdToBeDeleted) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListRestaurant list = listDao.findAllByUserAndId(userDb, listId);
        Restaurant rest = restaurantDao.findById(restaurantIdToBeDeleted);

        list.removeRestaurantFromList(rest);
        listDao.save(list);

        return "redirect:/" + listId;
    }
    @GetMapping("/review/{id}")
    public String reviewRestaurantForm(Model model, @PathVariable long id){
//        check to see if restaurant has reviews if so does logged in user have one for this restaurant
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restCheck = restaurantDao.findById(id);

        // **** Ad Error page for 404 ****
        if(restCheck == null){
            return "redirect:/"; // if User has review for restaurant then redirect to home
        }

        Review reviewCheck = reviewDao.findReviewByUserIdAndRestaurantId(reviewUser.getId(), id);

        if(reviewCheck == null){
            model.addAttribute("review", new Review());
        }else{
            model.addAttribute("review", reviewCheck);
        }
        model.addAttribute("id", id);
        model.addAttribute("restaurantName", restaurantDao.findById(id).getName());
        return "user/review";



    }
    @PostMapping("/review/{id}")
    public String saveReview(@ModelAttribute Review review, @PathVariable long id){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setUser(reviewUser);
        Restaurant revRestaurant = restaurantDao.findById(id);
        review.setRestaurant(revRestaurant);
        reviewDao.save(review);
//        have to redirect to specific list item
        return "redirect:/" + id;
    }
}
