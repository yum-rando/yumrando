package com.yumrando.app.controllers;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
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



    //Checking to see if the review is already in the system;
    // if it is, then just update the update_time column filled out;
    // if not, then create a review with the update_time column filled out;
    @PostMapping("restaurants/reviews")
    public List<Review> reviewUpdateTime(@RequestBody Restaurant restReviewToBeUpdated){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(restReviewToBeUpdated.getId());
        Restaurant restaurant = restaurantDao.findById(restReviewToBeUpdated.getId());
        Review reviewDb = reviewDao.findReviewByUserIdAndRestaurantId(userDb.getId(), restReviewToBeUpdated.getId());
        Date now = new Date();
        String pattern = "EEE, MMM d, yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String mysqlUpdateDate = formatter.format(now);

        //Checking to see if review exists
        if (reviewDb == null){
            //create a new review for the restaurant and update the time
            Review newReview = new Review();
            newReview.setUser(userDb);
            newReview.setRestaurant(restaurant);
            newReview.setUpdateTime(mysqlUpdateDate);
            reviewDao.save(newReview);
        } else {
            //update the update_time column in reviewDb
            reviewDb.setUpdateTime(mysqlUpdateDate);
            reviewDao.save(reviewDb);
        }
        return reviewDao.findAllByRestaurantId(restReviewToBeUpdated.getId());
    }

}
