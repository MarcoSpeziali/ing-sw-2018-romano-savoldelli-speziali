package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.utils.text.Localized;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class LobbyGUIController implements Initializable {

    private LobbyController proxyController;

    @FXML
    @Localized(key = Constants.Strings.LOBBY_TITLE, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label titleLabel;

    @FXML
    public JFXListView<Label> playersListView;

    @FXML
    public Label secondsLabel;

    @FXML
    @Localized(key = Constants.Strings.LOBBY_SECONDS_TEXT_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label secondsTextLabel;

    @FXML
    @Localized(key = Constants.Strings.LOBBY_WAITING_FOR_PLAYERS_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label waitingForPlayersLabel;

    @FXML
    @Localized(key = Constants.Strings.LOBBY_BACK_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton backButton;

    private FXMLLoader loader = new FXMLLoader();

    public void setProxy(LobbyController proxyController) {
        this.proxyController = proxyController;

        try {
            this.proxyController.init();
            setUpFuture();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Localized.Updater.update(this);
        this.titleLabel.setText(this.titleLabel.getText() + " - " + Settings.getSettings().getProtocol().toString());

        for (int i = 0; i < 4; i++) {
            Label lbl = new Label();
            lbl.setMinHeight(100);

            playersListView.getItems().add(lbl);
        }
    }

    @FXML
    public void onBackClicked() throws Exception {
        this.proxyController.close();

        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        SagradaGUI.primaryStage.show();
    }

    private void setUpFuture() {
        CompletableFuture<ILobby> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return this.proxyController.waitForUpdate();
            }
            catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        completableFuture.thenAccept(this::onUpdateReceived);
    }

    public void onUpdateReceived(ILobby update) {
        Platform.runLater(() -> {
            for (int i = 0; i < update.getPlayers().size(); i++) {
                IPlayer player = update.getPlayers().get(i);

                this.playersListView.getItems().get(i)
                        .setText(String.format("(%d) %s", player.getId(), player.getUsername()));
            }

            for (int i = update.getPlayers().size(); i < 4; i++) {
                this.playersListView.getItems().get(i).setText("");
            }

            setUpFuture();
        });
    }
}
