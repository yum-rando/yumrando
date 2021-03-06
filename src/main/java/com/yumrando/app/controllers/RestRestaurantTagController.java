package com.yumrando.app.controllers;


import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListRestaurantRepository;
import com.yumrando.app.repos.RestaurantRepository;
import com.yumrando.app.repos.TagRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
public class RestRestaurantTagController {
    private final UserRepository userDao;
    private final ListRestaurantRepository listRestaurantDao;
    private final TagRepository tagDao;
    private final RestaurantRepository restaurantDao;

    //   Dependency Injection for Restaurant Tag Controller
    public RestRestaurantTagController(UserRepository userDao, ListRestaurantRepository listRestaurantDao, TagRepository tagDao, RestaurantRepository restaurantDao) {
        this.userDao = userDao;
        this.listRestaurantDao = listRestaurantDao;
        this.tagDao = tagDao;
        this.restaurantDao = restaurantDao;
    }

    @GetMapping("/tags")
    ResponseEntity <Object> sendTags(){
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long activeUserId = activeUser.getId();

        List<RestaurantTag> allTagList = tagDao.findAll();



        List<RestaurantTag> filteredList = allTagList.stream().filter(tag -> tagDao.findByIdAndUsersId(tag.getId(), activeUserId) == null).collect(Collectors.toList());
        return new ResponseEntity<>(filteredList, HttpStatus.OK);
    }

    @PostMapping("/tags")
    ResponseEntity <Object> acceptSelectedTags(@RequestBody List<RestaurantTag> requestedRestList){
    User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    long userID = activeUser.getId();

        for (RestaurantTag requestedRest : requestedRestList ) {
            RestaurantTag userTag = tagDao.findById(requestedRest.getId());
            Set<User> setUsers = userTag.getUsers();
            if(setUsers == null) {
                Set<User> newUser = new HashSet<>();
                newUser.add(userDao.findById(userID));
                userTag.setUsers(newUser);
            }else{
                setUsers.add(userDao.findById(userID));
            }
                tagDao.save(userTag);
                User findUser = userDao.findById(userID);
                Set<RestaurantTag> setTags = findUser.getFavoriteTags();
                if(setTags == null){
                    Set<RestaurantTag> newTag = new HashSet<>();
                    newTag.add(userTag);
                    findUser.setFavoriteTags(newTag);
                } else {
                    setTags.add(userTag);
                }
                userDao.save(findUser);
        }
        return new ResponseEntity <>(requestedRestList, HttpStatus.OK);
    }


}