package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.*;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.controllers.SignInController;
import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.client.utils.text.TextInputControlPlaceholderLocalizationUpdater;
import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.client.controllers.proxies.socket.LobbySocketProxyController;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import it.polimi.ingsw.utils.text.Localized;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.NotBoundException;

import static com.jfoenix.controls.JFXDialog.DialogTransition.TOP;

public class SignInGUIView extends SignInController {

    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_USERNAME_FIELD_PROMPT_TEXT, fieldUpdater = TextInputControlPlaceholderLocalizationUpdater.class)
    public JFXTextField usernameField;
    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_PASSWORD_FIELD_PROMPT_TEXT, fieldUpdater = TextInputControlPlaceholderLocalizationUpdater.class)
    public JFXPasswordField passwordField;
    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_REGISTER_LINK_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Hyperlink registerLink;
    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_BACK_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton backButton;
    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_SING_IN_NOW_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label signInNowLabel;
    @FXML
    @Localized(key = Constants.Strings.WELCOME_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label welcomeLabel;
    @FXML
    @Localized(key = Constants.Strings.SIGN_IN_SIGN_IN_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton singInButton;
    @FXML
    public StackPane stackPane;

    private FXMLLoader loader = new FXMLLoader();

    @FXML
    public void initialize() {
        Localized.Updater.update(this);
    }

    @FXML
    public void onSignInClicked() throws IOException, NotBoundException {
        try {
            // TODO: execute in separate thread and create a loading indicator in the view
            super.onSignInRequested(this.usernameField.getText(), this.passwordField.getText(), () -> {
                try {
                    loader.setLocation(Constants.Resources.LOBBY_FXML.getURL());

                    LobbyController lobbyController;

                    if (Settings.getSettings().getProtocol() == Constants.Protocols.SOCKETS) {
                        lobbyController = new LobbySocketProxyController(
                                Settings.getSettings().getServerAddress(),
                                Settings.getSettings().getServerSocketPort(),
                                SignInManager.getManager().getToken()
                        );
                    }
                    else {
                        OneTimeRMIResponseProvider<LobbyInterface> oneTimeRMIResponseProvider = new OneTimeRMIResponseProvider<>(
                                Settings.getSettings().getServerAddress(),
                                Settings.getSettings().getServerRMIPort(),
                                LobbyInterface.class
                        );

                        lobbyController = oneTimeRMIResponseProvider.getSyncRemoteObject(EndPointFunction.LOBBY_JOIN_REQUEST_RMI, SignInManager.getManager().getToken());
                    }

                    Parent parent = loader.load();

                    LobbyGUIView lobbyGUIController = loader.getController();
                    lobbyGUIController.setProxy(lobbyController);

                    SagradaGUI.showStage(parent, 353, 546);
                }
                catch (IOException | NotBoundException e) {
                    FunctionalExceptionWrapper.wrap(e);
                }
            }, responseError -> {
                JFXDialogLayout content = new JFXDialogLayout();
                JFXDialog dialog = new JFXDialog(stackPane, content, TOP);
                JFXButton button = new JFXButton("OK");
                button.setOnAction(event -> dialog.close());

                if (responseError == ResponseFields.Error.UNAUTHORIZED) {
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_HEADER_TEXT)));
                    content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_CONTEXT_TEXT)));
                }
                else if (responseError == ResponseFields.Error.ALREADY_EXISTS) {
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ALREADY_EXISTS_TITLE)));
                    content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ALREADY_EXISTS_CONTEXT_TEXT)));
                }
                else {
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_HEADER_TEXT)));
                    content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT)));
                }
                content.setActions(button);
                dialog.show();
            });
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(NotBoundException.class)
                    .tryFinalUnwrap(IOException.class);
        }
    }

    @FXML
    public void onNotYetRegisteredClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_UP_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }

    @FXML
    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 550, 722);
    }

}

