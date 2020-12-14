package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) //This is needed since using the java.util.date
    private Date createdTime;

    @Column(length = 15)
    private String zipcode; //made to a string since zipcodes are not used for math

    @Column(columnDefinition = "TEXT")
    private String imgURL;

    @Column(length = 75)
    private String firstName;

    @Column
    private String lastName;

    //Still trying to figure out how to do this with List of List --> Below is not correct
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ListRestaurant> listOfRestaurant;
//
//    //Not to sure how to set this up with the 2 users referring to each other with the JPA
//    @ManyToMany
//    @JoinTable(
//        name = "friends",
//        joinColumns = {@JoinColumn(name = "user_friend_id")},
//        inverseJoinColumns = {@JoinColumn(name = "user_id")}
//    )
//    private List<User> friends;
//

    @ManyToMany
    @JoinTable(
        name = "user_favorite_tags",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<RestaurantTag> favoriteTags;


    //Constructors
    public User (){}

    //Create/Insert
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

//    //Read
//    public User(long id, String username, String password, String email, String phoneNumber, Date createdTime, String zipcode, String imgURL, String firstName, String lastName, List<ListRestaurant> listOfRestaurant, List<User> friends) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.createdTime = createdTime;
//        this.zipcode = zipcode;
//        this.imgURL = imgURL;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.listOfRestaurant = listOfRestaurant;
//        this.friends = friends;
//    }

    //Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
//
//    public List<ListRestaurant> getListOfRestaurant() {
//        return listOfRestaurant;
//    }
//
//    public void setListOfRestaurant(List<ListRestaurant> listOfRestaurant) {
//        this.listOfRestaurant = listOfRestaurant;
//    }
//
//    public List<User> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(List<User> friends) {
//        this.friends = friends;
//    }
}
