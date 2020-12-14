package com.yumrando.app.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class RestaurantTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Restaurant> restaurants;

    @ManyToMany(mappedBy = "favoriteTags")
    private List<User> users;

    //Constructors
    public RestaurantTag(){}

    //Insert/Create
    public RestaurantTag(String name, List<Restaurant> restaurants, List<User> users) {
        this.name = name;
        this.restaurants = restaurants;
        this.users = users;
    }

    //Read
    public RestaurantTag(long id, String name, List<Restaurant> restaurants, List<User> users) {
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

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

}

