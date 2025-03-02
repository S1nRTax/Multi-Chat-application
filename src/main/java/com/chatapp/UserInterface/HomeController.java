package com.chatapp.UserInterface;

import com.chatapp.models.connUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HomeController {
    private MainUI mainUI;

    @FXML
    private Text usernameText;
    @FXML
    private Text emailText;

    private StringProperty username = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();


    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
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

}
