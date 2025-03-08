package com.chatapp.models;


/**
 * This class represents the logged-in user, so basically an instance
 * will be passed after a successful login, that instance will have
 * username, email, list of friends...
 */

public class connUser {


    private String username;
    private String email;
    // list of friends.

    public connUser() {
    }

    public connUser(String username, String email) {
        this.username = username;
        this.email = email;
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
