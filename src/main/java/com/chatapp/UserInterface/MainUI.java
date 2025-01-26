package com.chatapp.UserInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainUI extends Application {

    private StackPane root; // Store the root StackPane
    private Scene scene; // Store the main scene

    @Override
    public void start(Stage stage) throws Exception {
        // Create a StackPane as the root
        root = new StackPane();

        // Load the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/chatapp/UserInterface/Assets/background-image.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);

        // Apply a GaussianBlur effect to the background image
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        backgroundImageView.setEffect(blur);

        // Add the blurred background to the StackPane
        root.getChildren().add(backgroundImageView);

        // Add the main content (e.g., Login UI)
        switchToLogin(); // Start with the Login scene

        // Create the scene
        scene = new Scene(root, 1280, 720);

        // Load the CSS file
        String css = this.getClass().getResource("/com/chatapp/UserInterface/Assets/RegisterLogin.css").toExternalForm();
        if (css != null && !css.isEmpty()) {
            scene.getStylesheets().add(css); // Add the CSS file to the scene
        } else {
            System.out.println("CSS file not found or path is incorrect.");
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Chat Application by S1ntax_");
        stage.show();
    }

    // Method to switch to the Login scene
    public void switchToLogin() {
        Login authUI = new Login(this); // Pass the MainUI instance
        BorderPane loginContent = authUI.createContent();
        root.getChildren().removeIf(node -> node instanceof BorderPane); // Remove existing content
        root.getChildren().add(loginContent); // Add the new content
    }

    // Method to switch to the Register scene
    public void switchToRegister() {
        Register authUI = new Register(this); // Pass the MainUI instance
        BorderPane registerContent = authUI.createContent();
        root.getChildren().removeIf(node -> node instanceof BorderPane); // Remove existing content
        root.getChildren().add(registerContent); // Add the new content
    }
}