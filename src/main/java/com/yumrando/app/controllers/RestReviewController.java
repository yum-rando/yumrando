package com.yumrando.app.controllers;

import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.web.bind.annotation.RestController;

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
