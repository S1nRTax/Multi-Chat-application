package com.chatapp.UserInterface;

import com.chatapp.models.connUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.slf4j.*;

import java.io.IOException;

public class Home {
    private MainUI mainUI;
    private connUser connectedUser;
    private static final Logger _logger = LoggerFactory.getLogger(Home.class);

    public Home(MainUI mainUI, connUser connectedUser) {
        this.connectedUser = connectedUser;
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
                controller.setConnectedUser(connectedUser);
            }
        } catch (IOException e) {
            _logger.error(e.getMessage());
        }

        return root;
    }
}