package com.chatapp.UserInterface;

import com.chatapp.Client.ChatClient;
import com.chatapp.models.Friend;
import com.chatapp.models.connUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HomeController {

    private static Logger _logger = LoggerFactory.getLogger(HomeController.class);

    private MainUI mainUI;
    private ChatClient chatClient;
    private boolean friendsListVisible = false;

    @FXML
    private Text usernameText;
    @FXML
    private Text emailText;
    @FXML
    private Button loginButton;
    @FXML
    private ListView<Friend> friendsListView; // Changed to Friend type

    private StringProperty username = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private List<Friend> currentFriends;

    public void setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
        this.chatClient = mainUI.getChatClient();
    }

    public void setConnectedUser(connUser connectedUser) {
        username.set(connectedUser.getUsername());
        email.set(connectedUser.getEmail());
        currentFriends = connectedUser.getFriendList();
        initializeFriendList();
        populateFriendsList(); // Added this method call
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
        usernameText.textProperty().bind(username);
        emailText.textProperty().bind(email);
        friendsListView.setVisible(false);
        friendsListView.setManaged(false);
    }

    private void initializeFriendList() {
        friendsListView.setCellFactory(new Callback<ListView<Friend>, ListCell<Friend>>() {
            @Override
            public ListCell<Friend> call(ListView<Friend> param) {
                return new ListCell<Friend>() {
                    @Override
                    protected void updateItem(Friend friend, boolean empty) {
                        super.updateItem(friend, empty);
                        if (empty || friend == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Button messageButton = new Button("Message");
                            messageButton.getStyleClass().add("friend-button");
                            messageButton.setOnAction(e -> handleMessageFriend(friend));

                            MenuButton optionsButton = new MenuButton("⋮");
                            optionsButton.getStyleClass().add("options-button");

                            MenuItem deleteItem = new MenuItem("Delete");
                            deleteItem.setOnAction(e -> handleDeleteFriend(friend));

                            optionsButton.getItems().addAll(deleteItem);

                            HBox buttonsContainer = new HBox(5, messageButton, optionsButton);
                            buttonsContainer.setStyle("-fx-alignment: CENTER_RIGHT;");

                            setText(friend.getUsername());
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        });
    }

    private void populateFriendsList() {
        friendsListView.getItems().clear();
        if (currentFriends != null && !currentFriends.isEmpty()) {
            friendsListView.getItems().addAll(currentFriends);
        } else {
            _logger.info("No friends available to display");
        }
    }

    @FXML
    private void handleFriendsButton(ActionEvent event) {
        friendsListVisible = !friendsListVisible;
        friendsListView.setVisible(friendsListVisible);
        friendsListView.setManaged(friendsListVisible);

        if (friendsListVisible) {
            refreshFriendsList();
        }
    }

    private void refreshFriendsList() {
        _logger.info("Refreshing friend list");
        populateFriendsList();
    }

    private void handleMessageFriend(Friend friend) {
        _logger.info("Messaging friend: " + friend.getUsername());
    }

    private void handleDeleteFriend(Friend friend) {
        _logger.info("Deleting friend: " + friend.getUsername());
        // Implement delete logic
        chatClient.sendMessage("/removefriend " + friend.getUsername());
        friendsListView.getItems().remove(friend);
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        chatClient.sendMessage("/logout ");
        initializeMessageHandler(username.get());
    }

    public void initializeMessageHandler(String username) {
        chatClient.getHandler().setMessageConsumer(message -> {
            Platform.runLater(() -> {
                if (message.startsWith("Logout successful")) {
                    _logger.info("Logout successful");
                    mainUI.switchToLogin();
                } else if (message.startsWith("Logout failed")) {
                    _logger.info("Logout failed");
                    showErrorPopup("Logout Failed", "Unable to log out. Please try again.");
                } else if (message.startsWith("Friend removed")) {
                    String removedUsername = message.substring("Friend removed ".length());
                    friendsListView.getItems().removeIf(f -> f.getUsername().equals(removedUsername));
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