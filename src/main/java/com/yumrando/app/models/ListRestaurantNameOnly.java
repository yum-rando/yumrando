package com.yumrando.app.models;

public class ListRestaurantNameOnly {
    private long id;
    private String name;
    private User user;

    public ListRestaurantNameOnly() {}

    public ListRestaurantNameOnly(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ListRestaurantNameOnly(long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

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
}
