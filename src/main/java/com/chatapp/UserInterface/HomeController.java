package com.chatapp.UserInterface;

import com.chatapp.Client.ChatClient;
import com.chatapp.models.connUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeController {

    private static Logger _logger = LoggerFactory.getLogger(HomeController.class);

    private MainUI mainUI;
    private ChatClient chatClient;

    @FXML
    private Text usernameText;
    @FXML
    private Text emailText;

    @FXML
    private Button loginButton;

    private StringProperty username = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();


    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
        this.chatClient = mainUI.getChatClient();
    }

    public void setConnectedUser(connUser connectedUser) {
        username.set(connectedUser.getUsername());
        email.set(connectedUser.getEmail());
    }

    // Getters for the properties
    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    @FXML
    public void initialize() {
        // Bind the username and email properties to the Text elements
        usernameText.textProperty().bind(username);
        emailText.textProperty().bind(email);
    }

    @FXML
    private void handleLoginButton(ActionEvent event){
        chatClient.sendMessage("/logout ");
        initializeMessageHandler(username.get());
    }

    public void initializeMessageHandler(String username){
        chatClient.getHandler().setMessageConsumer(message -> {
            Platform.runLater(() -> {
                if(message.startsWith("Logout successful")) {
                    _logger.info("Logout successful");
                    mainUI.switchToLogin();
                }else if(message.startsWith("Logout failed")) {
                    _logger.info("Logout failed");
                    showErrorPopup("Logout Failed", "Unable to log out. Please try again.");
                }
            });
        });
    }


    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
