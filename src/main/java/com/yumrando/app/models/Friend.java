package com.yumrando.app.models;

import javax.persistence.*;
import java.util.List;

//not too sure if this is set up correctly --> make sure to double check with team

@Entity
@Table(name = "friends")
public class Friend extends User {
    @ManyToMany(mappedBy = "user")
    private List<User> users;

    //Constructors
    public Friend(){
        super();
    }

    //Insert/Create/Read
    public Friend(List<User> users) {
        super();
        this.users = users;
    }

    //getters & setters
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
