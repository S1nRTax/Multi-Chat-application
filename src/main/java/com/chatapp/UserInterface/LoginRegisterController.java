package com.chatapp.UserInterface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginRegisterController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField newUsernameField;

    @FXML
    private PasswordField newPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic (if needed)
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Perform login logic here
        showAlert("Login Attempt", "Username: " + username + ", Password: " + password);
    }

    @FXML
    private void handleRegisterButtonAction() {
        String newUsername = newUsernameField.getText();
        String newPassword = newPasswordField.getText();

        // Perform registration logic here
        showAlert("Registration Attempt", "New Username: " + newUsername + ", New Password: " + newPassword);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}