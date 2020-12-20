package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private Set<RestaurantTag> favoriteTags;

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

    //Read
    public User(long id, String username, String password, String email, String phoneNumber, Date createdTime, String zipcode, String imgURL, String firstName, String lastName, List<ListRestaurant> listOfRestaurant, List<Review> reviews, Set<RestaurantTag> favoriteTags, List<FriendList> friends) {
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

    public Set<RestaurantTag> getFavoriteTags() {
        return favoriteTags;
    }

    public void setFavoriteTags(Set<RestaurantTag> favoriteTags) {
        this.favoriteTags = favoriteTags;
    }

    public List<FriendList> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendList> friends) {
        this.friends = friends;
    }

    //Many-To-Many Relationship Methods

    //Adding a tag to the User's Favorite
    public void addFavoriteTagToUser(RestaurantTag tag){
        this.favoriteTags.add(tag);
        //This by itself is referring to the current list object
        tag.getUsers().add(this);
    }
    //Removing a tage from the User's Favorite
    public void removeFavoriteTagFromUser(RestaurantTag tag){
        this.favoriteTags.remove(tag);
        //This by itself is referring to the current list object
        tag.getUsers().remove(this);
    }

    //Many-to-One Relationship Methods

    //Add a list to User
    public void addListToUser(ListRestaurant list){
        this.listOfRestaurant.add(list);
        list.setUser(this);
    }

    //Remove a list from user
    public void removeListFromUser(ListRestaurant list){
        this.listOfRestaurant.remove(list);
        list.setUser(this);
    }

    //Add a review to User
    public void addReviewToUser(Review review){
        this.reviews.add(review);
        review.setUser(this);
    }
    //Remove a review from User
    public void removeReviewFromUser(Review review){
        this.reviews.remove(review);
        review.setUser(this);
    }

    //Add a friend to User
    public void addFriendToUser(FriendList friend){
        this.friends.add(friend);
        friend.setUser(this);
    }

    //Remove a friend from User
    public void removeFriendFromUser(FriendList friend){
        this.friends.remove(friend);
        friend.setUser(this);
    }

}
