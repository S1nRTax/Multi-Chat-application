package com.chatapp.UserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class RegisterController {

    @FXML
    private Hyperlink loginLink; // Match the fx:id in FXML

    private MainUI mainUI; // Reference to the MainUI instance

    // Setter for MainUI
    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    @FXML
    private void handleLoginLink() {
        if (mainUI != null) {
            mainUI.switchToLogin(); // Switch to the Login scene
        } else {
            System.err.println("MainUI instance is null!");
        }
    }
}