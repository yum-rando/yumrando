package com.yumrando.app.controllers;

import com.yumrando.app.models.*;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private RestaurantRepository restaurantDao;
    private UserRepository userDao;
    private ListRestaurantRepository listDao;

    public ReviewController(ReviewRepository reviewDao, RestaurantRepository restaurantDao, UserRepository userDao, ListRestaurantRepository listDao){
        this.reviewDao = reviewDao;
        this.restaurantDao = restaurantDao;
        this.userDao = userDao;
        this.listDao = listDao;
    }
    //Showing all reviews for a specific restaurants
//    @GetMapping("/restaurants/{restaurantId}/reviews/")
//    List<Review> allReviewsForSpecificRestaurantShow(@PathVariable long restaurantId, Model vModel){
//        vModel.addAttribute("restaurantReviews", reviewDao.findAllByRestaurantId(restaurantId));
//        return reviewDao.findAllByRestaurantId(restaurantId);
//    }

    //viewing a specific review
    @GetMapping("/list/{listId}/restaurant/{restaurantId}/review")
    public String viewReview(@PathVariable long restaurantId, @PathVariable long listId, Model vModel){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        //Checks if it is located in the DB if not then create 1
        if (reviewCheck == null){
            vModel.addAttribute("review", new Review());
        } else {
            vModel.addAttribute("review", reviewCheck);
            System.out.println("reviewCheck.getId() = " + reviewCheck.getId()); //this is working
        }
        vModel.addAttribute("restaurantId", restaurantId);
        vModel.addAttribute("restaurantName", restaurantDao.findById(restaurantId).getName());
        vModel.addAttribute("listId", listId);
        return "user/review";
    }

    //updating the review
    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review")
    public String submitReview(@ModelAttribute Review reviewToBeSaved, @PathVariable long restaurantId, @PathVariable long listId){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant reviewRest = restaurantDao.findById(restaurantId);
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        Date now = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String mysqlUpdateDate = formatter.format(now);
        if (reviewCheck == null){
            reviewToBeSaved.setUser(reviewUser);
            reviewToBeSaved.setRestaurant(reviewRest);
            reviewDao.save(reviewToBeSaved);
        } else {
            reviewCheck.setRating(reviewToBeSaved.getRating());
            reviewCheck.setComment(reviewToBeSaved.getComment());
            reviewCheck.setUpdateTime(mysqlUpdateDate);
            reviewDao.save(reviewCheck);
        }
        return "redirect:/" + listId; //this needs to be the list the restaurant in id
    }

    //Adding Pictures and Deleting Pictures from a review

    //Deleting Review --> not needed but will add anyway
    @PostMapping("/delete/list/{listId}/restaurant/{restaurantId}/review")
    public String deleteRestaurantFromList(@PathVariable long listId, @PathVariable long restaurantId) {
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        if (reviewCheck.getPhotos() != null){
            for (Photo photo : reviewCheck.getPhotos() ) {
                reviewCheck.getPhotos().remove(photo);
            }
        }
        reviewDao.deleteById(reviewCheck.getId());
        return "redirect:/" + listId;
    }
}
