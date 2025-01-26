package com.chatapp.UserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class LoginController {

    @FXML
    private Hyperlink registerLink; // Match the fx:id in FXML

    private MainUI mainUI; // Reference to the MainUI instance

    // Setter for MainUI
    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    @FXML
    private void handleRegisterLink() {
        if (mainUI != null) {
            mainUI.switchToRegister(); // Switch to the Register scene
        } else {
            System.err.println("MainUI instance is null!");
        }
    }
}