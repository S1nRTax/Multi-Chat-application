package com.chatapp.UserInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.*;
import com.chatapp.Client.ChatClient;

public class MainUI extends Application {
    private ChatClient chatClient;
    private static final Logger _logger = LoggerFactory.getLogger(MainUI.class);
    private StackPane root; // Store the root StackPane
    private Scene scene; // Store the main scene

    @Override
    public void start(Stage stage) throws Exception {
        root = new StackPane();

        // Start the ChatClient when the application launches
        chatClient = new ChatClient("localhost", 8080);
        new Thread(() -> {
            try {
                chatClient.start();
            } catch (InterruptedException e) {
                _logger.error("Failed to start the Client: {}", e.getMessage(), e);
            }
        }, "ChatClient Thread").start();


        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/chatapp/UserInterface/Assets/background-image.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        backgroundImageView.setEffect(blur);

        root.getChildren().add(backgroundImageView);

        switchToLogin();

        // Create the scene
        scene = new Scene(root, 1280, 720);

        // Load the CSS file
        String css = this.getClass().getResource("/com/chatapp/UserInterface/Assets/RegisterLogin.css").toExternalForm();
        if (css != null && !css.isEmpty()) {
            scene.getStylesheets().add(css);
        } else {
            _logger.warn("Unable to load stylesheet");
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Chat Application by S1ntax_");
        stage.show();
    }

    // Method to switch to the Login scene
    public void switchToLogin() {
        Login authUI = new Login(this);
        BorderPane loginContent = authUI.createContent();
        root.getChildren().removeIf(node -> node instanceof BorderPane);
        root.getChildren().add(loginContent);
    }

    // Method to switch to the Register scene
    public void switchToRegister() {
        Register authUI = new Register(this);
        BorderPane registerContent = authUI.createContent();
        root.getChildren().removeIf(node -> node instanceof BorderPane);
        root.getChildren().add(registerContent);
    }

    public ChatClient getChatClient() {
        return this.chatClient;
    }

    // Method to switch to the Home scene.

}