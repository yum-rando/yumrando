//package com.yumrando.app.models;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "photos")
//public class Photo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String url;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(name = "review_id")
//    private Review review;
//
//
//    //Constructors
//    public Photo() {}
//
//    //Insert/Create
//    public Photo(String url, String description) {
//        this.url = url;
//        this.description = description;
//    }
//
//    //Read
//    public Photo(long id, String url, String description, Review review) {
//        this.id = id;
//        this.url = url;
//        this.description = description;
//        this.review = review;
//    }
//
//    //getters & setters
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Review getReview() {
//        return review;
//    }
//
//    public void setReview(Review review) {
//        this.review = review;
//    }
//}
