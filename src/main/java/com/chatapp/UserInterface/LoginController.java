package com.chatapp.UserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import com.chatapp.Client.ChatClient;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.slf4j.*;

public class LoginController {
    private static final Logger _logger = LoggerFactory.getLogger(LoginController.class);


    @FXML
    private Hyperlink registerLink;

    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;


    private MainUI mainUI; // Reference to the MainUI instance
    private ChatClient chatClient;

    // Setter for MainUI
    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
        this.chatClient = mainUI.getChatClient();
    }

    @FXML
    private void handleRegisterLink() {
        if (mainUI != null) {
            mainUI.switchToRegister(); // Switch to the Register scene
        } else {
            System.err.println("MainUI instance is null!");
        }
    }

    @FXML
    private void handleLogin() {
        new Thread(() -> {
            try {
                while (!chatClient.isConnected()) {
                    Thread.sleep(100);
                }
                // Now send the login message
                String username = emailTextField.getText();
                String password = passwordField.getText();
                _logger.info("Username is: {}", username);
                _logger.info("Password is: {}", password);
                chatClient.sendMessage("/login " + username + " " + password);
            } catch (Exception e) {
                _logger.error(e.getMessage(), e);
            }
        }, "Login Thread").start();
    }

}