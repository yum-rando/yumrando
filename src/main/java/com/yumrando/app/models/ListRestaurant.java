package com.yumrando.app.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Restaurant> restaurants;

    //Constructors
    public ListRestaurant() {}

    //Insert/Create
    public ListRestaurant(String name, User user, List<Restaurant> restaurants) {
        this.name = name;
        this.user = user;
        this.restaurants = restaurants;
    }

    //Read
    public ListRestaurant(long id, String name, User user, List<Restaurant> restaurants) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }


}
