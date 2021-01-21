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
    public String viewReview(@PathVariable long restaurantId, @PathVariable long listId, Model vModel, @RequestParam(name = "friend") String confirm){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        List<Photo> reviewPhotos = photoDao.findAllByReview(reviewCheck);

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
        vModel.addAttribute("photos", reviewPhotos);
        vModel.addAttribute("friendId", confirm );

        return "user/review";
    }

    //updating the review
    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review/{friendId}")
    public String submitReview(@ModelAttribute Review reviewToBeSaved, @PathVariable long restaurantId, @PathVariable long listId, @PathVariable String friendId, @RequestParam (name = "photo_image_url") String photoUrl){

        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant reviewRest = restaurantDao.findById(restaurantId);
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        Photo newPhoto = new Photo(photoUrl);
        Date now = new Date();
        String patternReal = "EEE, MMM d, yyyy";
        SimpleDateFormat formatterReal = new SimpleDateFormat(patternReal);
        String mysqlUpdateDate = formatterReal.format(now);
        if (reviewCheck == null){
            reviewToBeSaved.setUser(reviewUser);
            reviewToBeSaved.setRestaurant(reviewRest);
            reviewDao.save(reviewToBeSaved);
            newPhoto.setReview(reviewToBeSaved);
            if (newPhoto.getUrl().isEmpty()){
                newPhoto.setUrl(null);
            }
            photoDao.save(newPhoto);
            if (newPhoto.getUrl() == null){
                photoDao.deleteById(newPhoto.getId());
            }
        } else {
            reviewCheck.setRating(reviewToBeSaved.getRating());
            reviewCheck.setComment(reviewToBeSaved.getComment());
            reviewCheck.setUpdateTime(mysqlUpdateDate);
            newPhoto.setReview(reviewCheck);
            if (newPhoto.getUrl().isEmpty()){
                newPhoto.setUrl(null);
            }
            photoDao.save(newPhoto);
            if (newPhoto.getUrl() == null){
                photoDao.deleteById(newPhoto.getId());
            }
            reviewDao.save(reviewCheck);
        }

        if(friendId.equals("0")){
            return "redirect:/" + listId;
        }else {
           return "redirect:/friend/" + friendId + "/list/" + listId;
        }
    }

    //Deleting Review --> not needed but will add anyway
    @PostMapping("/delete/list/{listId}/restaurant/{restaurantId}/review")
    public String deleteRestaurantFromList(@PathVariable long listId, @PathVariable long restaurantId) {
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        List<Photo> photosCheck = photoDao.findAllByReview(reviewCheck);
        if (photosCheck != null){
            photoDao.deleteAllByReview(reviewCheck); //works with the @Transactional annotation
        }
        reviewDao.deleteById(reviewCheck.getId());
        return "redirect:/" + listId;
    }

    //Deleting a Photo
    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review/photos/{photoId}/{friendId}/delete")
    public String deletePhotoFromReview(@PathVariable long listId, @PathVariable long restaurantId, @PathVariable long photoId, @PathVariable String friendId){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        photoDao.deleteById(photoId);

        if(friendId.equals("0")){
            return "redirect:/list/" + listId + "/restaurant/" + restaurantId + "/review/?friend=" + friendId;
        }else {
            return "redirect:/friend/" + friendId + "/list/" + listId;
        }
    }

    //Delete all photos
    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review/{friendId}/photos/delete-all")
    public String deleteAllPhotosFromReview(@PathVariable long listId, @PathVariable long restaurantId, @PathVariable String friendId){
        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
        List<Photo> photosCheck = photoDao.findAllByReview(reviewCheck);
        if (photosCheck != null){
            photoDao.deleteAllByReview(reviewCheck); //works with the @Transactional annotation
        }
        if(friendId.equals("0")){
            return "redirect:/list/" + listId + "/restaurant/" + restaurantId + "/review/?friend=" + friendId;
        }else {
            return "redirect:/friend/" + friendId + "/list/" + listId;
        }
    }
}
