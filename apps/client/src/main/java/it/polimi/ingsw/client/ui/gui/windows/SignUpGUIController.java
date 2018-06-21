package it.polimi.ingsw.client.ui.gui.windows;


import com.jfoenix.controls.*;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.SignUpController;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;


public class SignUpGUIController extends SignUpController {

    @FXML
    public JFXTextField user;
    @FXML
    public JFXPasswordField pass;
    @FXML
    public JFXPasswordField repeatPass;
    @FXML
    public StackPane stackPane;
    private FXMLLoader loader = new FXMLLoader();

    public SignUpGUIController() {
    }

    @FXML
    public void onSignUpClicked() {

        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(stackPane, content, CENTER);
        JFXButton button = new JFXButton("OK");
        button.setOnAction(event -> dialog.close());
        content.setActions(button);
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

                    if (error == ResponseFields.Error.ALREADY_EXISTS) {
                        content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_HEADER_TEXT)));
                        content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_CONTENT_TEXT)));
                    }
                    else {
                        content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_HEADER_TEXT)));
                        content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT)));
                    }

                    dialog.show();
                });
            }
            else {
                if (pass.getText().length() < 8 || pass.getText().contains(" ")) {

                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_CREDENTIALS_PROPERTIES_ERROR_TITLE)));
                    content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_CREDENTIAL_PROPERTIES_ERROR_CONTENT_TEXT)));
                    dialog.show();

                }
                else {
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_TITLE)));
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_CONTENT_TEXT)));
                    dialog.show();
                }
            }
        }
        else {
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_USER_PROPERTIES_ERROR_TITLE)));
            content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_USER_PROPERTIES_ERROR_CONTENT_TEXT)));
            dialog.show();
        }
    }

    @FXML
    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }
}