package com.chatapp.UserInterface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.util.Duration;

public class RegisterLoginController {

    @FXML
    private SplitPane loginRegisterSplitPane; // Ensure this matches the fx:id in your FXML

    public void initialize() {
        // Check if the SplitPane is properly injected
        if (loginRegisterSplitPane == null) {
            System.out.println("SplitPane is not initialized. Check fx:id in FXML.");
            return;
        }


    }
}