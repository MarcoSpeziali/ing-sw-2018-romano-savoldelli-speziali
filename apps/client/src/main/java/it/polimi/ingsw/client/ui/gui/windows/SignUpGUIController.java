package it.polimi.ingsw.client.ui.gui.windows;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.SignUpController;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;

// TODO: handle pass != repeatPass
// TODO: handle localization
// TODO: handle username rules (min 8 chars, no spaces, ..?)
// TODO: handle password rules (min 8 chars, no spaces, ..?)
public class SignUpGUIController extends SignUpController {

    private FXMLLoader loader = new FXMLLoader();

    @FXML public JFXTextField user;
    @FXML public JFXPasswordField pass;
    @FXML public JFXPasswordField repeatPass;

    public SignUpGUIController() {}

    @FXML
    public void onSignUpClicked() {
        if (pass.getText().equals(repeatPass.getText())) {
            super.onSignUpRequested(user.getText(), pass.getText(), () -> {
                try {
                    this.onBackClicked();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);

                if (error == ResponseFields.Error.ALREADY_EXISTS) {
                    alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_TITLE));
                    alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_HEADER_TEXT));
                    alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_CONTEXT_TEXT));
                }
                else {
                    alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_TITLE));
                    alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_HEADER_TEXT));
                    alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT));
                }

                alert.showAndWait();
            });
        }
    }

    @FXML
    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720,480));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        //SagradaGUI.primaryStage.initStyle(StageStyle.UNDECORATED);
        SagradaGUI.primaryStage.show();
    }
}