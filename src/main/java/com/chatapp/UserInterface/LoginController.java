package com.chatapp.UserInterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import com.chatapp.Client.ChatClient;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import org.slf4j.*;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class LoginController {
    private static final Logger _logger = LoggerFactory.getLogger(LoginController.class);

    @javafx.fxml.FXML
    private Text errorLabel;

    @javafx.fxml.FXML
    private Hyperlink registerLink;


    @javafx.fxml.FXML
    private TextField emailTextField;

    @javafx.fxml.FXML
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
                if(username.isEmpty()){
                    showError("username must not be empty!");
                }else if(password.isEmpty()){
                    showError("password must not be empty!");
                }
                chatClient.sendMessage("/login " + username + " " + password);
                initializeMessageHandler();
            } catch (Exception e) {
                _logger.error(e.getMessage(), e);
            }
        }, "Login Thread").start();
    }


    private void initializeMessageHandler() {
        // Set up the message consumer to handle server responses
        chatClient.getHandler().setMessageConsumer(message -> {
            Platform.runLater(() -> {
                if (message.startsWith("Login successful")) {
                    _logger.debug("Login successful");
                    mainUI.switchToHome();
                } else if (message.startsWith("Authentication Failed")) {
                    String[] parts = message.split(":",2);
                    _logger.info("Login failed: {}", message);
                    showError(parts[1]);
                } else if(message.startsWith("Internal error")) {
                    showError(message);
                }
            });
        });
    }

    private void showError(String errorMessage) {

        if(errorMessage.trim().startsWith("Invalid credentials. Please try again.")) {
            emailTextField.setText("");
            passwordField.setText("");
            emailTextField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        }else if(errorMessage.trim().startsWith("Incorrect password")){
            passwordField.setText("");
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        }else if(errorMessage.startsWith("Internal error")) {
                passwordField.setText("");
                emailTextField.setText("");
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
        }else if(errorMessage.trim().equals("username must not be empty!")){
            emailTextField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        }else if (errorMessage.trim().equals("password must not be empty!")){
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        }




        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    emailTextField.setStyle("-fx-border-color: none; -fx-border-width: 2px;");
                    passwordField.setStyle("-fx-border-color: none; -fx-border-width: 2px;");
                    errorLabel.setVisible(false);
                    errorLabel.setText("");
                });
            }
        }, 3500);
    }

}