package com.yumrando.app.models;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

//This is still a work in progress. I'm not too sure how to make a model of this since it is giving me an error message

@Entity
@Table(name = "lists")
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private List<ArrayList<Restaurant>> listOfList = new ArrayList<ArrayList<Restaurant>>();
}
