package it.polimi.ingsw.client.ui.gui.windows;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SignUpGUIController {

    private FXMLLoader loader = new FXMLLoader();
    private SignInManager model;

    @FXML public JFXTextField user;
    @FXML public JFXPasswordField pass;
    @FXML public JFXPasswordField repeatPass;

    public SignUpGUIController() {}

    public void onSignUpClicked() {
        // TODO implement signup
        String username = this.user.getText();
        String password = this.pass.getText();
        System.out.println("user: "+username+"\npassword: "+password);
    }

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL()); // TODO check relative path for controller in all FXMLs
        this.setScene(new Scene(loader.load(), 720,480));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        //SagradaGUI.primaryStage.initStyle(StageStyle.UNDECORATED);
        SagradaGUI.primaryStage.show();
    }
}