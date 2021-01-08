package com.yumrando.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String apiId;

    @Column(nullable = false)
    private String name;

    @Column(length = 15, unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String website;

    @Column
    private String address;

    @Column
    private String city;
    
    @Column(length = 15)
    private String zipcode;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) //This is needed since using the java.util.date
    private Date createdTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<Review> reviews;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    //new table will be created with the combining of columns of list_id and restaurant_id
    @JoinTable(
            name = "list_restaurants",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "list_id")}
    )
    //still working on this to be a list of list of restaurants
    private Set<ListRestaurant> lists;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "restaurant_tags",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<RestaurantTag> tags;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "restaurant_userTag",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "userTag_id")}
    )
    private Set<UserTag> userTagsSet;

    //Constructors
    public Restaurant(){}

    //Create/Insert
    public Restaurant(String name, String address, String city, Set<ListRestaurant> lists, Set<RestaurantTag> tags) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.lists = lists;
        this.tags = tags;
    }

    //Read
    public Restaurant(long id, String apiId, String name, String phoneNumber, String website, String address, String city, String zipcode, Date createdTime, List<Review> reviews, Set<ListRestaurant> lists, Set<RestaurantTag> tags, Set<UserTag> userTagsSet) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.createdTime = createdTime;
        this.reviews = reviews;
        this.lists = lists;
        this.tags = tags;
        this.userTagsSet = userTagsSet;
    }

    //Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Set<ListRestaurant> getLists() {
        return lists;
    }

    public void setLists(Set<ListRestaurant> lists) {
        this.lists = lists;
    }

    public Set<RestaurantTag> getTags() {
        return tags;
    }

    public void setTags(Set<RestaurantTag> tags) {
        this.tags = tags;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<UserTag> getUserTagsSet(){
        return userTagsSet;
    }
    public void setUserTagsSet(Set<UserTag> userTagsSet){
        this.userTagsSet = userTagsSet;
    }


    //Many-To-Many Relationship Methods

    //Adding a list to Restaurant
    public void addListToRestaurant (ListRestaurant listRestaurant){
        this.lists.add(listRestaurant);
        //This by itself is referring to the current list object
        listRestaurant.getRestaurants().add(this);
    }

    //Deleting a list from Restaurant
    public void removeListFromRestaurant (ListRestaurant listRestaurant){
        this.lists.remove(listRestaurant);
        //This by itself is referring to the current list object
        listRestaurant.getRestaurants().remove(this);
    }

    //Adding a tag to Restaurant
    public void addTagToRestaurant (RestaurantTag tag){
        this.tags.add(tag);
        //This by itself is referring to the current list object
        tag.getRestaurants().add(this);

    }

    //Deleting a tag from Restaurant
    public void removeTagFromRestaurant (RestaurantTag tag){
        this.tags.remove(tag);
        //This by itself is referring to the current list object
        tag.getRestaurants().remove(this);
    }

    //Adding custom tag to Restaurant
    public void addCustomTagToRestaurant(UserTag customTag){
        this.userTagsSet.add(customTag);
        // This referring to current object
        customTag.getRestaurants().add(this);
    }
    public void removingCustomTagFromRestaurant(UserTag customTag){
        this.userTagsSet.remove(customTag);
        // This referring to restaurant object
        customTag.getRestaurants().remove(this);
    }

    //Many-to-One Relationship Methods

    //Add a review to Restaurant
    public void addReviewToRestaurant(Review review){
        this.reviews.add(review);
        review.setRestaurant(this);
    }

    //Remove a review from Restaurant
    public void removeReviewFromRestaurant(Review review){
        this.reviews.remove(review);
        review.setRestaurant(this);
    }
}
