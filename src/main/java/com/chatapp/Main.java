package com.chatapp;

import com.chatapp.UserInterface.RegisterLogin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Application started");

        // Launch the JavaFX application
        RegisterLogin.launch(RegisterLogin.class, args);
    }
}