package com.yumrando.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

   // Jakarta Bean constraint for username
    @Size(max = 99, min = 1, message = "A username must be no longer than 99 characters and longer than 1")
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    // Jakarta Bean constraint for password
    @Size(max = 200, message = "A password must be no longer than 200 characters")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ListRestaurant> listOfRestaurant;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
        name = "user_favorite_tags",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<RestaurantTag> favoriteTags;

    @OneToMany(mappedBy = "user")
    private List<FriendList> friends;

    public User(User copy) {
        id = copy.id;
//        email = copy.email;
        username = copy.username;
        password = copy.password;
    }

    //Constructors
    public User (){}

    //Create/Insert
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String username, String password, String confirmPassword){
        this.username = username;
        this.password = password;
    }

    //Read
    public User(long id, String username, String password, String email, String phoneNumber, Date createdTime, String zipcode, String imgURL, String firstName, String lastName, List<ListRestaurant> listOfRestaurant, List<Review> reviews, List<RestaurantTag> favoriteTags, List<FriendList> friends) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdTime = createdTime;
        this.zipcode = zipcode;
        this.imgURL = imgURL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.listOfRestaurant = listOfRestaurant;
        this.reviews = reviews;
        this.favoriteTags = favoriteTags;
        this.friends = friends;
    }

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

    public List<ListRestaurant> getListOfRestaurant() {
        return listOfRestaurant;
    }

    public void setListOfRestaurant(List<ListRestaurant> listOfRestaurant) {
        this.listOfRestaurant = listOfRestaurant;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<RestaurantTag> getFavoriteTags() {
        return favoriteTags;
    }

    public void setFavoriteTags(List<RestaurantTag> favoriteTags) {
        this.favoriteTags = favoriteTags;
    }

    public List<FriendList> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendList> friends) {
        this.friends = friends;
    }

}
