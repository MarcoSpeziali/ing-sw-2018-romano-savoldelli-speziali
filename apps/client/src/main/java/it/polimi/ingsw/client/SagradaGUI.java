package it.polimi.ingsw.client;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class SagradaGUI extends Application {

    public static Stage primaryStage;
    public static double xOffset = 0;
    public static double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        Parent root = loader.load();
        FadeTransition ft = new FadeTransition(Duration.millis(2000), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        primaryStage = stage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        showStage(root, 550, 722);
    }

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
        primaryStage.show();
    }

}