package com.yumrando.app.controllers;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class RestReviewController {
    private UserRepository userDao;
    private RestaurantRepository restaurantDao;
    private ReviewRepository reviewDao;

    public RestReviewController(UserRepository userDao, RestaurantRepository restaurantDao, ReviewRepository reviewDao) {
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
        this.reviewDao = reviewDao;
    }

    @CrossOrigin
    @GetMapping("restaurants/{restaurantId}/reviews/")
    List<Review> allReviewsForSpecificRestaurantShow(@PathVariable long restaurantId){return reviewDao.findAllByRestaurantId(restaurantId);}

    @CrossOrigin
    @GetMapping("restaurants/{restaurantId}/reviews/{reviewId}")
    Review specificReviewForRestaurant(@PathVariable long restaurantId, @PathVariable long reviewId){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reviewDao.findReviewByUserIdAndRestaurantId(userDb.getId(), restaurantId);
    }

    //Checking to see if the review is already in the system;
    // if it is, then just update the update_time column filled out;
    // if not, then create a review with the update_time column filled out;
    @PatchMapping("restaurants/{restaurantId}/reviews/{reviewId}")
    public Review reviewUpdateTime(
            @RequestBody Review reviewToBeUpdated,
            @PathVariable long restaurantId,
            @PathVariable long reviewId
    ){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantDao.findById(restaurantId);
        Review reviewDb = reviewDao.findReviewByUserIdAndRestaurantId(userDb.getId(), restaurantId);
        Date now = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String mysqlUpdateDate = formatter.format(now);

        //Checking to see if review exists
        if (reviewDb == null){
            //create a new review for the restaurant and update the time
            reviewToBeUpdated.setUser(userDb);
            reviewToBeUpdated.setRestaurant(restaurant);
            reviewToBeUpdated.setUpdateTime(mysqlUpdateDate);
            reviewDao.save(reviewToBeUpdated);
        } else {
            //update the update_time column in reviewDb
            reviewDb.setUpdateTime(mysqlUpdateDate);
            reviewDao.save(reviewDb);
        }
        return reviewDao.findById(reviewId);
    }

    //This is for the REVIEW CONTROLLER
    //UPDATING THE DATE IN THE SYSTEM --> MADE IT A STRING INSTEAD OF A DATE SINCE IT WAS MESSING UP WITH THE HIBERNATE
//    private void updateReviewTime(Review review){
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
