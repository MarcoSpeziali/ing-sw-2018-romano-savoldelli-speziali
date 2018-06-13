package it.polimi.ingsw.client.ui.gui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SettingsGUIController {
    private FXMLLoader loader = new FXMLLoader();
    private SignInManager model;

    public SettingsGUIController() {}


    public void onSaveClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_UP_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 550, 722));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        //SagradaGUI.primaryStage.initStyle(StageStyle.UNDECORATED);
        SagradaGUI.primaryStage.show();
    }
}
