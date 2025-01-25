package com.chatapp.UserInterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RegisterLogin {

    public BorderPane createContent() {
        BorderPane root = new BorderPane();

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/UserInterface/RegisterLogin.fxml"));
            Parent fxmlRoot = loader.load();
            root.setCenter(fxmlRoot);
            RegisterLoginController controller = loader.getController();
            if (controller != null) {
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