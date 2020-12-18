package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long apiId;

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
    private Date chosenTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<Review> reviews;

    @ManyToMany(cascade = CascadeType.ALL)
    //new table will be created with the combining of columns of list_id and restaurant_id
    @JoinTable(
            name = "list_restaurants",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "list_id")}
    )
    //still working on this to be a list of list of restaurants
    private List<ListRestaurant> lists;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "restaurant_tags",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<RestaurantTag> tags;

    //Constructors
    public Restaurant(){}

    //Create/Insert
    public Restaurant(String name, String address, String city, List<ListRestaurant> lists, List<RestaurantTag> tags) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.lists = lists;
        this.tags = tags;
    }

    //Read
    public Restaurant(long id, long apiId, String name, String phoneNumber, String website, String address, String city, String zipcode, Date chosenTime, List<Review> reviews, List<ListRestaurant> lists, List<RestaurantTag> tags) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.chosenTime = chosenTime;
        this.reviews = reviews;
        this.lists = lists;
        this.tags = tags;
    }

    //Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
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

    public Date getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(Date chosenTime) {
        this.chosenTime = chosenTime;
    }

    public List<ListRestaurant> getLists() {
        return lists;
    }

    public void setLists(List<ListRestaurant> lists) {
        this.lists = lists;
    }

    public List<RestaurantTag> getTags() {
        return tags;
    }

    public void setTags(List<RestaurantTag> tags) {
        this.tags = tags;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
