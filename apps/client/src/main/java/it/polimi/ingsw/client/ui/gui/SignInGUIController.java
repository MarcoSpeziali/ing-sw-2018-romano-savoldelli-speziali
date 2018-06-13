package it.polimi.ingsw.client.ui.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.TimeoutException;

public class SignInGUIController {
    private FXMLLoader loader = new FXMLLoader();
    @FXML public JFXTextField user;
    @FXML public JFXPasswordField pass;

    public SignInGUIController() {}

    public void onSignInClicked() {
        try {
            if (SignInManager.getManager().signIn(user.getText(), pass.getText())) {
                System.out.println("Logged as\nuser: " + user.getText() + "\npassword: " + pass.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Access denied");
                alert.setHeaderText("Wrong Username/Password combination.");
                alert.setContentText("Please retry with different combination or sign up as a new user");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
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

