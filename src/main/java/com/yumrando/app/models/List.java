package com.yumrando.app.models;

import javax.persistence.*;
import java.util.ArrayList;

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
