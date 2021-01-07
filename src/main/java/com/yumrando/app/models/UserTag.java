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

    @ManyToMany(mappedBy = "userTagsSet", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Restaurant> restaurants;

//     Empty Constructor
    public UserTag (){}

//     Create/Update Constructor
    public UserTag (String name, User user, Set<Restaurant> restaurants){
        this.name = name;
        this.user = user;
        this.restaurants = restaurants;
    }

//    Read Constructor
    public UserTag (long id, String name, User user, Set<Restaurant> restaurants) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.restaurants = restaurants;
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

    public Set<Restaurant> getRestaurants(){
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void addingRestaurantToCustomTag(Restaurant restaurant){
        this.restaurants.add(restaurant);
        // this referencing UserTagSet object
        restaurant.getUserTagsSet().add(this);
    }
    public void removeRestaurantFromCustomTag(Restaurant restaurant){
        this.restaurants.remove(restaurant);
        //this referencing UserTag object
        restaurant.getUserTagsSet().remove(this);
    }
}