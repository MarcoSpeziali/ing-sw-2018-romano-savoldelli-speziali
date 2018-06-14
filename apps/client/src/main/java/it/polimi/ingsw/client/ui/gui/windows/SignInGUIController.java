package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.client.utils.text.TextInputControlPlaceholderLocalizationUpdater;
import it.polimi.ingsw.utils.text.LocalizedText;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.io.IOException;
import java.rmi.NotBoundException;

public class SignInGUIController {

    private FXMLLoader loader = new FXMLLoader();

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_USERNAME_FIELD_PROMPT_TEXT, fieldUpdater = TextInputControlPlaceholderLocalizationUpdater.class)
    public JFXTextField usernameField;

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_PASSWORD_FIELD_PROMPT_TEXT, fieldUpdater = TextInputControlPlaceholderLocalizationUpdater.class)
    public JFXPasswordField passwordField;

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_REGISTER_LINK_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Hyperlink registerLink;

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_BACK_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton backButton;

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_SING_IN_NOW_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label signInNowLabel;

    @FXML
    @LocalizedText(key = Constants.Strings.WELCOME_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label welcomeLabel;

    @FXML
    @LocalizedText(key = Constants.Strings.SIGN_IN_SIGN_IN_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton singInButton;

    @FXML
    public void initialize() {
        LocalizedText.Updater.update(this);
    }

    public void onSignInClicked() throws IOException {
        try {
            if (SignInManager.getManager().signIn(usernameField.getText(), passwordField.getText())) {
                ClientLogger.getLogger(SignInGUIController.class).info(String.format("Logged as user: %s password: %s", usernameField.getText(), passwordField.getText()));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_TITLE));
                alert.setHeaderText(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_HEADER_TEXT));
                alert.setContentText(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_CONTEXT_TEXT));
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        loader.setLocation(Constants.Resources.LOBBY.getURL());
        Parent root = loader.load();
        LobbyGUIController controller = loader.getController();
        controller.startTimeline(); // FIXME timeline comes from server
        this.setScene(new Scene(root, 910, 720));
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
        SagradaGUI.primaryStage.show();
    }
}

