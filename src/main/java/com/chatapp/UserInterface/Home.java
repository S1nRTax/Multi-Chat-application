package com.chatapp.UserInterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane; // Use StackPane instead of BorderPane
import org.slf4j.*;

import java.io.IOException;

public class Home {
    private MainUI mainUI;
    private static final Logger _logger = LoggerFactory.getLogger(Home.class);

    public Home(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    public AnchorPane createContent() {
        AnchorPane root = new AnchorPane();
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/UserInterface/Home.fxml"));
            Parent fxmlRoot = loader.load();
            root.getChildren().add(fxmlRoot);

            HomeController controller = loader.getController();
            if (controller != null) {
                controller.setMainUI(mainUI);
            }
        } catch (IOException e) {
            _logger.error(e.getMessage());
        }

        return root;
    }
}