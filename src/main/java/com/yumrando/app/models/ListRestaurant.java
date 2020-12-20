package com.yumrando.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lists")
public class ListRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "lists")
    private Set<Restaurant> restaurants;

    //Constructors
    public ListRestaurant() {}

    //Insert/Create
    public ListRestaurant(String name, User user, Set<Restaurant> restaurants) {
        this.name = name;
        this.user = user;
        this.restaurants = restaurants;
    }

    public ListRestaurant(String name){
        this.name = name;
    }

    //Read
    public ListRestaurant(long id, String name, User user, Set<Restaurant> restaurants) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.restaurants = restaurants;
    }

    //getters & setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
@JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    //Many-To-Many Relationship Methods

    //Adding a restaurant to the ListOfRestaurants
    public void addRestaurantToList(Restaurant restaurant){
        this.restaurants.add(restaurant);
        //This by itself is referring to the current list object
        restaurant.getLists().add(this);
    }

    //Removing a restaurant from the ListOfRestaurants
    public void removeRestaurantFromList(Restaurant restaurant){
        this.restaurants.remove(restaurant);
        //This by itself is referring to the current list object
        restaurant.getLists().remove(this);
    }

}
