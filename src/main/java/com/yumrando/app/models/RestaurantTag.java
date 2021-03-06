package com.yumrando.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tags")
public class RestaurantTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Restaurant> restaurants;

    @JsonIgnore
    @ManyToMany(mappedBy = "favoriteTags")
    private Set<User> users;

    //Constructors
    public RestaurantTag(){}

    public RestaurantTag(String name){
        this.name = name;
    }

    //Insert/Create
    public RestaurantTag(String name, Set<Restaurant> restaurants, Set<User> users) {
        this.name = name;
        this.restaurants = restaurants;
        this.users = users;
    }

    //Read
    public RestaurantTag(long id, String name, Set<Restaurant> restaurants, Set<User> users) {
        this.id = id;
        this.name = name;
        this.restaurants = restaurants;
        this.users = users;
    }


    //Getters & Setters
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

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }
    @JsonIgnore
    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    //Many-To-Many Relationship Methods

    //Adding a Restaurant to the RestaurantTags
    public void addRestaurantToTag(Restaurant restaurant){
        this.restaurants.add(restaurant);
        //This by itself is referring to the current list object
        restaurant.getTags().add(this);
    }
    //Removing a Restaurant from a RestaurantTags
    public void removeRestaurantFromTag(Restaurant restaurant){
        this.restaurants.remove(restaurant);
        //This by itself is referring to the current list object
        restaurant.getTags().remove(this);
    }
    
}

