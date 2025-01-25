package com.chatapp;

import com.chatapp.UserInterface.MainUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("Application started");

        // start the UI :
        MainUI.launch(MainUI.class, args);
    }
}