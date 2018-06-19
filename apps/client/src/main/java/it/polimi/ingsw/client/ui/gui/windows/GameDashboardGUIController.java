package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import it.polimi.ingsw.client.Constants;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import java.io.IOException;

public class GameDashboardGUIController extends Application {

    private FXMLLoader loader = new FXMLLoader();

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public JFXHamburger hamburger;
    @FXML
    public JFXDrawer drawer;

    public Parent root;


    public static Stage primaryStage;
    public static double xOffset = 0;
    public static double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        loader.setLocation(Constants.Resources.GAME_DASHBOARD_FXML.getURL());

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Pane main = (Pane) root.getChildren().get(0);
        //root.setCenter(toolCardGUIView.render());
        primaryStage = stage;
        showStage(root, 1280, 720);
    }



















    public static void showStage(Parent root, int width, int height) {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        HamburgerBackArrowBasicTransition hmb = new HamburgerBackArrowBasicTransition(hamburger);
        hmb.setRate(1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            hmb.setRate(hmb.getRate()*-1);
            hmb.play();
            if (drawer.isOpened())
                drawer.close();
            else drawer.open();
        });
    }*/
}
