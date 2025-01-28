package com.chatapp.UserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import com.chatapp.Client.ChatClient;
import org.slf4j.*;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class RegisterController {
    private static final Logger _logger = LoggerFactory.getLogger(RegisterController.class);

    @FXML
    private Hyperlink loginLink;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
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
    private void handleLoginLink() {
        if (mainUI != null) {
            mainUI.switchToLogin(); // Switch to the Login scene
        } else {
            System.err.println("MainUI instance is null!");
        }
    }


    @FXML
    private void handleRegister(){
        new Thread(() -> {
            try{
                while (!chatClient.isConnected()) {
                    Thread.sleep(100);
                }

                String username = usernameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                chatClient.sendMessage("/register " + username + " " + email + " " + password);
            }catch (Exception e){
                _logger.error(e.getMessage(), e);
            }
        }, "Register thread").start();
    }
}