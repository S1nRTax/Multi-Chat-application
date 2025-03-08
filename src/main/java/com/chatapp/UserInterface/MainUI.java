package com.chatapp.UserInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.*;
import com.chatapp.Client.ChatClient;
import com.chatapp.models.connUser;

public class MainUI extends Application {
    private ChatClient chatClient;
    private static final Logger _logger = LoggerFactory.getLogger(MainUI.class);
    private StackPane root;
    private Scene scene;
    private ImageView backgroundImageView;

    @Override
    public void start(Stage stage) throws Exception {
        root = new StackPane();

        chatClient = new ChatClient("localhost", 8080);
        new Thread(() -> {
            try {
                chatClient.start();
            } catch (InterruptedException e) {
                _logger.error("Failed to start the Client: {}", e.getMessage(), e);
            }
        }, "ChatClient Thread").start();

        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/chatapp/UserInterface/Assets/background-image.png"));
        backgroundImageView = new ImageView(backgroundImage);

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        backgroundImageView.setEffect(blur);

        root.getChildren().add(backgroundImageView);

        scene = new Scene(root, 1280, 720);

        // Load the CSS file
        String css = this.getClass().getResource("/com/chatapp/UserInterface/Assets/RegisterLogin.css").toExternalForm();
        if (css != null && !css.isEmpty()) {
            scene.getStylesheets().add(css);
        } else {
            _logger.warn("Unable to load stylesheet");
        }

        // Call switchToLogin AFTER the scene is created
        switchToLogin();

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Chat Application by S1ntax_");
        stage.show();
    }

    // Method to switch to the Login scene
    public void switchToLogin() {
        Login authUI = new Login(this);
        BorderPane loginContent = authUI.createContent();
        // Remove only the content nodes (excluding the background image)
        root.getChildren().removeIf(node -> node != backgroundImageView);
        root.getChildren().add(loginContent);
        String loginCss = this.getClass().getResource("/com/chatapp/UserInterface/Assets/RegisterLogin.css").toExternalForm();
        if (loginCss != null && !loginCss.isEmpty()) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(loginCss);
        } else {
            _logger.warn("Unable to load RegisterLogin.css stylesheet");
        }
    }

    // Method to switch to the Register scene
    public void switchToRegister() {
        Register authUI = new Register(this);
        BorderPane registerContent = authUI.createContent();
        root.getChildren().removeIf(node -> node != backgroundImageView);
        root.getChildren().add(registerContent);
        String registerCss = this.getClass().getResource("/com/chatapp/UserInterface/Assets/RegisterLogin.css").toExternalForm();
        if (registerCss != null && !registerCss.isEmpty()) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(registerCss);
        } else {
            _logger.warn("Unable to load RegisterLogin.css stylesheet");
        }
    }

    // Method to switch to the Home tab
    public void switchToHome(connUser connectedUser) {
        _logger.debug("Switching to home screen for user: {}", connectedUser.getUsername());
        Home home = new Home(this, connectedUser);
        AnchorPane homeContent = home.createContent();
        homeContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Remove only the content nodes (excluding the background image)
        root.getChildren().removeIf(node -> node != backgroundImageView);
        root.getChildren().add(homeContent);
        String homeCss = this.getClass().getResource("/com/chatapp/UserInterface/Assets/Home.css").toExternalForm();
        if (homeCss != null && !homeCss.isEmpty()) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(homeCss);
        } else {
            _logger.warn("Unable to load Home.css stylesheet");
        }
    }

    public ChatClient getChatClient() {
        return this.chatClient;
    }
}