package com.yumrando.app.controllers;

import com.yumrando.app.models.Photo;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.PhotoRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.ReviewRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PhotoController {
    private PhotoRepository photoDao;
    private ReviewRepository reviewDao;
    private RestaurantRepository restaurantDao;
    private ListRestaurantRepository listDao;

    public PhotoController(PhotoRepository photoDao, ReviewRepository reviewDao, RestaurantRepository restaurantDao, ListRestaurantRepository listDao) {
        this.photoDao = photoDao;
        this.reviewDao = reviewDao;
        this.restaurantDao = restaurantDao;
        this.listDao = listDao;
    }

    //Adding photo (1)
//    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review/photos/add")
//    public Photo addPhoto(@PathVariable long listId, @PathVariable long restaurantId, @ModelAttribute Photo photoToBeSaved) {
//        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
//        photoToBeSaved.setReview(reviewCheck);
//        photoDao.save(photoToBeSaved);
//        return photoToBeSaved;
//    }

    //Adding Multiple Photos

    //Deleting photo (1)
//    @PostMapping("/list/{listId}/restaurant/{restaurantId}/review/photos/delete")
//    public Photo deletePhoto(@PathVariable long listId, @PathVariable long restaurantId){
//        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
//
//    }

    //Deleting Multiple Photos

//    @GetMapping("/list/{listId}/restaurant/{restaurantId}/review/photos")
//    public String viewPhotos(@PathVariable long listId, @PathVariable long restaurantId, Model vModel){
//        User reviewUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Review reviewCheck = reviewDao.findAllByUserIdAndRestaurantId(reviewUser.getId(), restaurantId);
//        List<Photo> photos = photoDao.findAllByReview(reviewCheck);
//        vModel.addAttribute("listId", listId);
//        vModel.addAttribute("restaurantId", restaurantId);
//        vModel.addAttribute("photos", photos);
//        return "user/review";
//    }
}
