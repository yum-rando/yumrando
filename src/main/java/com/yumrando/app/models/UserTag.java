package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_tags")
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "restaurants", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Restaurant> restaurant;

//     Empty Constructor
    public UserTag (){}

//     Create/Update Constructor
    public UserTag (String name, User user, Set<Restaurant> restaurant){
        this.name = name;
        this.user = user;
        this.restaurant = restaurant;
    }

//    Read Constructor
    public UserTag (long id, String name, User user, Set<Restaurant> restaurant) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.restaurant = restaurant;
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

    public Set<Restaurant> getRestaurant(){
        return restaurant;
    }

    public void setRestaurant(Set<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }
}