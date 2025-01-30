package com.chatapp.UserInterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import com.chatapp.Client.ChatClient;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterController {
    private static final Logger _logger = LoggerFactory.getLogger(RegisterController.class);

    @FXML private Hyperlink loginLink;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Text errorLabel;

    private MainUI mainUI; // Reference to the MainUI instance
    private ChatClient chatClient;

    // Setter for MainUI
    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
        this.chatClient = mainUI.getChatClient();
        initializeMessageHandler(); // Initialize message handler after setting chatClient
    }

    @FXML
    private void handleLoginLink() {
        if (mainUI != null) {
            mainUI.switchToLogin(); // Switch to the Login scene
        } else {
            _logger.error("MainUI instance is null!");
        }
    }

    @FXML
    private void handleRegister() {
        new Thread(() -> {
            try {
                waitForClientConnection();

                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();

                if (validateInputs(username, email, password)) {
                    chatClient.sendMessage("/register " + username + " " + email + " " + password);
                }
            } catch (Exception e) {
                _logger.error("Error during registration: {}", e.getMessage(), e);
            }
        }, "RegisterThread").start();
    }

    private void waitForClientConnection() throws InterruptedException {
        while (!chatClient.isConnected()) {
            Thread.sleep(100);
        }
    }

    private boolean validateInputs(String username, String email, String password) {
        if (username.isEmpty()) {
            showError("username must not be empty!", usernameField);
            return false;
        }
        if (email.isEmpty()) {
            showError("email must not be empty!", emailField);
            return false;
        }
        if (password.isEmpty()) {
            showError("password must not be empty!", passwordField);
            return false;
        }
        return true;
    }

    private void initializeMessageHandler() {
        chatClient.getHandler().setMessageConsumer(message -> {
            Platform.runLater(() -> {
                if (message.startsWith("Registration successful")) {
                    handleRegistrationSuccess(message);
                } else if (message.startsWith("Problem registering") || message.startsWith("Internal error")) {
                    handleRegistrationError(message);
                }
            });
        });
    }

    private void handleRegistrationSuccess(String message) {
        _logger.debug("Registration successful");
        mainUI.switchToLogin();
    }

    private void handleRegistrationError(String message) {
        _logger.info("Registration failed: {}", message);
        String[] parts = message.split(":", 2);
        showError(parts.length > 1 ? parts[1] : message, null);
    }

    private void showError(String errorMessage, TextField field) {
        Platform.runLater(() -> {
            if (field != null) {
                field.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            }
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        usernameField.setStyle("-fx-border-color: none; -fx-border-width: 2px;");
                        emailField.setStyle("-fx-border-color: none; -fx-border-width: 2px;");
                        passwordField.setStyle("-fx-border-color: none; -fx-border-width: 2px;");
                        errorLabel.setVisible(false);
                        errorLabel.setText("");
                    });
                }
            }, 5000);
        });
    }
}