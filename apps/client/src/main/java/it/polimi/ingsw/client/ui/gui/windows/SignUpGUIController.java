package it.polimi.ingsw.client.ui.gui.windows;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.SignUpController;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;

import java.io.IOException;


public class SignUpGUIController extends SignUpController {

    @FXML
    public JFXTextField user;
    @FXML
    public JFXPasswordField pass;
    @FXML
    public JFXPasswordField repeatPass;
    private FXMLLoader loader = new FXMLLoader();

    public SignUpGUIController() {
    }

    @FXML
    public void onSignUpClicked() {

        if (user.getText().length() > 8 && !user.getText().contains(" ")) {

            if (pass.getText().equals(repeatPass.getText()) && pass.getText().length() >= 8 && !pass.getText().contains(" ")) {
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
                        alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_TITLE));
                        alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_HEADER_TEXT));
                        alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_CONTENT_TEXT));
                    }
                    else {
                        alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_TITLE));
                        alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_HEADER_TEXT));
                        alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT));
                    }

                    alert.showAndWait();
                });
            }
            else {
                if (pass.getText().length() < 8 || pass.getText().contains(" ")) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_CREDENTIALS_PROPERTIES_ERROR_TITLE));
                    //alert.setHeaderText("");
                    alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_CREDENTIAL_PROPERTIES_ERROR_CONTENT_TEXT));
                    alert.showAndWait();

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_TITLE));
                    //alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_HEADER_TEXT));
                    alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_CONTENT_TEXT));
                    alert.showAndWait();

                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_USER_PROPERTIES_ERROR_TITLE));
            //alert.setHeaderText("");
            alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_USER_PROPERTIES_ERROR_CONTENT_TEXT));
            alert.showAndWait();
        }
    }

    @FXML
    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }
}