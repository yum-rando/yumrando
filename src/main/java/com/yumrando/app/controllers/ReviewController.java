package com.yumrando.app.controllers;

import com.yumrando.app.models.*;
import com.yumrando.app.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private PhotoRepository photoDao;
    //private final String fileStackApiKey;

    public ReviewController(ReviewRepository reviewDao, RestaurantRepository restaurantDao, UserRepository userDao, ListRestaurantRepository listDao, PhotoRepository photoDao){
        this.reviewDao = reviewDao;
        this.restaurantDao = restaurantDao;
        this.userDao = userDao;
        this.listDao = listDao;
        this.photoDao = photoDao;
    }

    @Value("${filestack.key}")
    private String fileStackApiKey;

    @GetMapping("/keys.js")
    @ResponseBody
    public String apikey(){
        System.out.println(fileStackApiKey);
        return "const FileStackApiKey = `" + fileStackApiKey + "`";
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
        List<Photo> reviewPhotos = photoDao.findAllByReview(reviewCheck);

        //Checks if it is located in the DB if not then create 1
        if (reviewCheck == null){
            vModel.addAttribute("review", new Review());
            //vModel.addAttribute("photo", new Photo());
        } else {
            vModel.addAttribute("review", reviewCheck);
            System.out.println("reviewCheck.getId() = " + reviewCheck.getId()); //this is working
        }
        vModel.addAttribute("restaurantId", restaurantId);
        vModel.addAttribute("restaurantName", restaurantDao.findById(restaurantId).getName());
        vModel.addAttribute("listId", listId);
        vModel.addAttribute("photos", reviewPhotos);

        return "user/review";
    }

    //updating the review
    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review")
    public String submitReview(@ModelAttribute Review reviewToBeSaved, @PathVariable long restaurantId, @PathVariable long listId, @RequestParam (name = "photo_image_url") String photoUrl){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant reviewRest = restaurantDao.findById(restaurantId);
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        Photo newPhoto = new Photo(photoUrl);
        List<Photo> reviewPhoto = photoDao.findAllByReview(reviewCheck);
        Date now = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String mysqlUpdateDate = formatter.format(now);
        if (reviewCheck == null){
            reviewToBeSaved.setUser(reviewUser);
            reviewToBeSaved.setRestaurant(reviewRest);
            reviewDao.save(reviewToBeSaved);
            newPhoto.setReview(reviewToBeSaved);
            photoDao.save(newPhoto);

        } else {
            reviewCheck.setRating(reviewToBeSaved.getRating());
            reviewCheck.setComment(reviewToBeSaved.getComment());
            reviewCheck.setUpdateTime(mysqlUpdateDate);
            newPhoto.setReview(reviewCheck);
            photoDao.save(newPhoto);
            //reviewCheck.setPhotos(reviewToBeSaved.getPhotos());
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
