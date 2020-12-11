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

    @Column
    private String website;

    @Column
    private String address;

    @Column(length = 50)
    private String state;

    @Column(length = 15)
    private String zipcode;

    @Column(columnDefinition = "CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) //This is needed since using the java.util.date
    private Date chosenTime;

    @ManyToMany(cascade = CascadeType.ALL)
    //new table will be created with the combining of columns of list_id and restaurant_id
    @JoinTable(
            name = "list_restaurants",
            joinColumns = {@JoinColumn(name = "list_id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_id")}
    )
    private List<ListRestaurant> lists;

    //Constructors
    public Restaurant(){}

    //Create/Insert


    //Read


    //Getters & Setters
}
