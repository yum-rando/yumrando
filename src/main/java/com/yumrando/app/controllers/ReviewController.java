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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


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

    @PostMapping("delete/restaurants/reviews/{restaurantId}/{reviewId}")
    //Since the review requires both the user id and the restaurant id, you don't really need the review id
    public String deleteReview(@PathVariable long restaurantId, @PathVariable long reviewId){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewToBeDeleted = reviewDao.findReviewByUserIdAndRestaurantId(userDb.getId(), restaurantId);
        //Review reviewToBeDeletedB = reviewDao.findReviewByUserIdAndId(userDb.getId(), reviewId);
        reviewDao.deleteById(reviewToBeDeleted.getId());
        //reviewDao.deleteById(reviewToBeDeletedB.getId());
        return "redirect:/profile";
    }

    //Adding Pictures and Deleting Pictures from a review


}
