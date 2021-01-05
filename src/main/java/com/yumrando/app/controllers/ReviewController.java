package com.yumrando.app.controllers;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private RestaurantRepository restaurantDao;
    private UserRepository userDao;

    public ReviewController(ReviewRepository reviewDao, RestaurantRepository restaurantDao, UserRepository userDao){
        this.reviewDao = reviewDao;
        this.restaurantDao = restaurantDao;
        this.userDao = userDao;
    }

    @GetMapping("/restaurants/{restaurantId}/reviews/")
    List<Review> allReviewsForSpecificRestaurantShow(@PathVariable long restaurantId, Model vModel){
        vModel.addAttribute("restaurantReviews", reviewDao.findAllByRestaurantId(restaurantId));
        return reviewDao.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
    Review specificReviewForRestaurant(@PathVariable long restaurantId, @PathVariable long reviewId){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reviewDao.findReviewByUserIdAndRestaurantId(userDb.getId(), restaurantId);
    }

    //Adding Pictures and Deleting Pictures from a review


}
