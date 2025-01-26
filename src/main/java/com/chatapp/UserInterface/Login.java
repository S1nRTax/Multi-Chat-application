package com.chatapp.UserInterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Login {

    private MainUI mainUI; // Reference to the MainUI instance

    public Login(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    public BorderPane createContent() {
        BorderPane root = new BorderPane();

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/UserInterface/Login.fxml"));
            Parent fxmlRoot = loader.load();
            root.setCenter(fxmlRoot);

            // Pass the MainUI instance to the controller
            LoginController controller = loader.getController();
            if (controller != null) {
                controller.setMainUI(mainUI); // Set the MainUI instance
                System.out.println("FXML controller loaded successfully.");
            }
        } catch (IOException e) {
            // Handle errors during FXML loading
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }

        return root;
    }
}