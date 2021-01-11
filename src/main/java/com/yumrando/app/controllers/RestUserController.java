package com.yumrando.app.controllers;

import com.yumrando.app.models.FriendList;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.ListFriendsRepository;
import com.yumrando.app.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class RestUserController {

    private UserRepository userDao;
    private ListFriendsRepository friendDao;

    public RestUserController(UserRepository userDao, ListFriendsRepository friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    @ExceptionHandler({IllegalStateException.class, NullPointerException.class})
    public void handleException(){

    }

    @PostMapping("/profile/friends/create")
    ResponseEntity<Object> addFriend(@RequestBody User user) {
        User friend = userDao.findByUsername(user.getUsername());
        if (friend == null){
            return new ResponseEntity<>("User doesn't exist.", HttpStatus.NOT_FOUND);
        }
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getUsername().equals(currUser.getUsername())){
            return new ResponseEntity<>(user, HttpStatus.UNAUTHORIZED);
        }
        FriendList friendCheck = friendDao.findAllByUserIdAndFriendId(currUser.getId(), friend.getId());
        FriendList inverseCheck = friendDao.findAllByUserIdAndFriendId(friend.getId(), currUser.getId());
        if ((friendCheck != null) || (inverseCheck != null)){
            return new ResponseEntity<>(friendCheck, HttpStatus.UNAUTHORIZED);
        } else {
            FriendList addFriend = new FriendList(currUser, friend, false);
            friendDao.save(addFriend);
            return new ResponseEntity<>(addFriend, HttpStatus.OK);
        }
    }
}

