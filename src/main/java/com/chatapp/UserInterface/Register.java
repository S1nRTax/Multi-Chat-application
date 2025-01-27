package com.chatapp.UserInterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.slf4j.*;

import java.io.IOException;

public class Register {
    private static final Logger _logger = LoggerFactory.getLogger(Register.class);
    private MainUI mainUI; // Reference to the MainUI instance

    public Register(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    public BorderPane createContent() {
        BorderPane root = new BorderPane();

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/UserInterface/Register.fxml"));
            Parent fxmlRoot = loader.load();
            root.setCenter(fxmlRoot);

            // Pass the MainUI instance to the controller
            RegisterController controller = loader.getController();
            if (controller != null) {
                controller.setMainUI(mainUI); // Set the MainUI instance
            }else{
                _logger.error("FXML controller could not be loaded.");
            }
        } catch (IOException e) {
            // Handle errors during FXML loading
            _logger.error(e.getMessage(), e);
        }

        return root;
    }
}