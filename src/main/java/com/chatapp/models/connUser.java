package com.chatapp.models;


import java.util.List;

/**
 * This class represents the logged-in user, so basically an instance
 * will be passed after a successful login, that instance will have
 * username, email, list of friends...
 */

public class connUser {

    private int id;
    private String username;
    private String email;
    private List<Friend> friendList;

    public connUser() {
    }



    public connUser(int id, String username, String email, List<Friend> friendList) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.friendList = friendList;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
