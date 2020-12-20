package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, length = 1)
    private int rating;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) //This is needed since using the java.util.date
    private Date createTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "review")
    private List<Photo> photos;

    @ManyToOne
    private User user;

    @ManyToOne
    private Restaurant restaurant;

    //Constructors
    public Review(){}

    //insert/Create
    public Review(int rating, User user, Restaurant restaurant) {
        this.rating = rating;
        this.user = user;
        this.restaurant = restaurant;
    }

    //Read
    public Review(long id, String comment, int rating, Date createTime, List<Photo> photos, User user, Restaurant restaurant) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.createTime = createTime;
        this.photos = photos;
        this.user = user;
        this.restaurant = restaurant;
    }

    //Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    //Many-to-One Relationship Methods

    //Adding a Photo to a Review
    public void addPhotoToReview(Photo photo){
        this.photos.add(photo);
        photo.setReview(this);
    }
    //Removing a Photo from a Review
    public void removePhotoFromReview(Photo photo){
        this.photos.remove(photo);
        photo.setReview(this);
    }

    //Check Ties with User and Restaurant -->Not sure about this yet
//    public void userRestaurantReview(User user, Restaurant restaurant){
//        user.getReviews().add(this);
//        restaurant.getReviews().add(this);
//    }

}
