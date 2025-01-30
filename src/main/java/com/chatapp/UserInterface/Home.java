package com.chatapp.UserInterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane; // Use StackPane instead of BorderPane
import org.slf4j.*;

import java.io.IOException;

public class Home {
    private MainUI mainUI;
    private static final Logger _logger = LoggerFactory.getLogger(Home.class);

    public Home(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    public StackPane createContent() { // Change return type to StackPane
        StackPane root = new StackPane(); // Use StackPane instead of BorderPane

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/UserInterface/Home.fxml"));
            Parent fxmlRoot = loader.load();
            root.getChildren().add(fxmlRoot); // Add the FXML content to the StackPane

            HomeController controller = loader.getController();
            if (controller != null) {
                controller.setMainUI(mainUI); // Set the MainUI instance
            }
        } catch (IOException e) {
            _logger.error(e.getMessage());
        }

        return root;
    }
}