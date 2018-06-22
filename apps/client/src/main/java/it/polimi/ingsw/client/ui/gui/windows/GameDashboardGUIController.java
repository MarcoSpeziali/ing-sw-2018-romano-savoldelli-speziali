package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import it.polimi.ingsw.client.Constants;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameDashboardGUIController extends Application {
    public static Stage primaryStage;
    public static double xOffset = 0;
    public static double yOffset = 0;


    public Parent root;
    @FXML
    public GridPane cardsPane;
    @FXML
    public GridPane windowsPane;

    private FXMLLoader loader = new FXMLLoader();


    public static void showStage(Parent root, int width, int height) {
        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        loader.setLocation(Constants.Resources.GAME_DASHBOARD_FXML.getURL());

        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
        Node card = loader.load();
        

        primaryStage = stage;
        showStage(root, 1280, 720);
    }


}
