package com.chatapp.UserInterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import com.chatapp.Client.ChatClient;
import org.slf4j.*;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.util.Timer;
import java.util.TimerTask;


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



    private void initializeMessageHandler() {
        // Set up the message consumer to handle server responses
        chatClient.getHandler().setMessageConsumer(message -> {
            Platform.runLater(() -> {
                if (message.startsWith("Registration successful")) {
                    _logger.info("Registration successful");
                    mainUI.switchToLogin();
                } else if (message.startsWith("Problem registering")) {
                    showError(message);
                } else if(message.startsWith("Internal error")) {
                    showError(message);
                }
            });
        });
    }

    private void showError(String errorMessage) {

        //errorLabel.setText(errorMessage);
       // errorLabel.setVisible(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //errorLabel.setVisible(false);
                    //errorLabel.setText("");
                });
            }
        }, 3000);
    }
}