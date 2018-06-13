package it.polimi.ingsw.client.ui.gui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SignInGUIController {
    private FXMLLoader loader = new FXMLLoader();
    private SignInManager model;
    @FXML public JFXTextField user;
    @FXML public JFXPasswordField pass;

    public SignInGUIController() {}

    public void onSignInClicked() {
        // TODO implement signin
        String username = this.user.getText();
        String password = this.pass.getText();
        System.out.println("user: "+username+"\npassword: "+password);
    }

    public void onNotYetRegisteredClicked() throws IOException {
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

