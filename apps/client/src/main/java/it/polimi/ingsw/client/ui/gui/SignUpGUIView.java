package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpGUIView extends Application {
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.SIGN_UP_FXML.getURL());

        // Create the Pane and all Details
        BorderPane root = loader.load(Constants.Resources.SIGN_UP_FXML.getURL().openStream());

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        stage.setScene(scene);
        // Set the Title to the Stage
        stage.setTitle("A simple FXML Example");
        // Display the Stage
        stage.show();
    }
}