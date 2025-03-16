package com.chatapp.models;

public class Friend {

    private int id;
    private String username;
    private enum Status {
        online, offline, away
    }

    public Friend(){
    }

    public Friend(String username, int id) {
        this.username = username;
        this.id = id;
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
}
