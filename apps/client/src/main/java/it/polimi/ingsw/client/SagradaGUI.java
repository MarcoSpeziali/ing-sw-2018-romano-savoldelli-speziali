package it.polimi.ingsw.client;

import javafx.animation.FadeTransition;
import javafx.application.Application;
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
    public double xOffset = 0;
    public double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        Parent root = loader.load();
        FadeTransition ft = new FadeTransition(Duration.millis(2000), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        Scene scene = new Scene(root, 550, 722);

        primaryStage = stage;
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    public void onMousePressed(MouseEvent event) { // FIXME not correct offsets
        xOffset = primaryStage.getX() + event.getScreenX();
        yOffset = primaryStage.getY() + event.getScreenY();
    }

    public void onMouseDragged(MouseEvent event) { // FIXME not correct offsets
        primaryStage.setX(event.getScreenX() + xOffset);
        primaryStage.setY(event.getScreenY() + yOffset);
    }
}