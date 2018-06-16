package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.LobbyController;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.utils.text.LocalizedText;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class LobbyGUIController extends LobbyController implements Initializable {

    @FXML
    @LocalizedText(key = Constants.Strings.LOBBY_TITLE, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label titleLabel;

    @FXML
    public JFXListView<Label> playersListView;

    @FXML
    public Label secondsLabel;

    @FXML
    @LocalizedText(key = Constants.Strings.LOBBY_SECONDS_TEXT_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label secondsTextLabel;

    @FXML
    @LocalizedText(key = Constants.Strings.LOBBY_WAITING_FOR_PLAYERS_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label waitingForPlayersLabel;

    @FXML
    @LocalizedText(key = Constants.Strings.LOBBY_BACK_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton backButton;

    private FXMLLoader loader = new FXMLLoader();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalizedText.Updater.update(this);

        for (int i = 0; i < 4; i++) {
            try {
                Label lbl = new Label();
                lbl.setMinHeight(100);

                playersListView.getItems().add(lbl);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        SagradaGUI.primaryStage.show();
    }

    @Override
    public void onTimerStarted(int duration) throws RemoteException {

    }

    @Override
    public void onTimerStopped() throws RemoteException {

    }

    @Override
    public void onMatchStarting(int matchId) throws RemoteException {

    }

    @Override
    public void onUpdateReceived(ILobby update) throws RemoteException {
        for (int i = 0; i < update.getPlayers().size(); i++) {
            IPlayer player = update.getPlayers().get(i);
            this.playersListView.getItems().get(i).setText(String.format("(%d) %s", player.getId(), player.getUsername()));
        }
    }
}
