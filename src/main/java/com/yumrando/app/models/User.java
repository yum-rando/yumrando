package com.yumrando.app.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) //This is needed since using the java.util.date
    private Date createdTime;

    @Column(length = 15)
    private String zipcode; //made to a string since zipcodes are not used for math

    @Column(columnDefinition = "TEXT")
    private String imgURL;

    @Column(length = 75)
    private String firstName;

    @Column
    private String lastName;










    //Constructors
    public User (){}

    //Create/Insert


    //Read


    //Getters & Setters

}
