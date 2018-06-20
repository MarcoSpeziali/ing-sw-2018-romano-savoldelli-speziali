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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class LobbyGUIController implements Initializable {

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
    private LobbyController proxyController;
    private FXMLLoader loader = new FXMLLoader();

    private Timer timer = new Timer();
    private int remainingTime = -1;

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
            lbl.setStyle("-fx-alignment: CENTER; " +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14;");

            playersListView.getItems().add(lbl);
        }

        this.secondsLabel.setText("");
        this.secondsTextLabel.setOpacity(0);
    }

    @FXML
    public void onBackClicked() throws Exception {
        this.proxyController.close();

        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 550, 722);
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
            for (int i = 0; i < update.getPlayers().length; i++) {
                IPlayer player = update.getPlayers()[i];

                this.playersListView.getItems().get(i)
                        .setText(String.format("(%d) %s", player.getId(), player.getUsername()));
            }

            for (int i = update.getPlayers().length; i < 4; i++) {
                this.playersListView.getItems().get(i).setText("");
            }

            if (update.getTimeRemaining() > 0) {
                this.secondsLabel.setText(String.valueOf(update.getTimeRemaining()));
                this.secondsTextLabel.setOpacity(1);

                if (this.remainingTime == -1) {
                    this.startTimer();
                }

                this.remainingTime = update.getTimeRemaining();
            }
            else {
                this.secondsLabel.setText("");
                this.secondsTextLabel.setOpacity(0);

                this.remainingTime = -1;
                this.stopTimer();
            }

            setUpFuture();
        });
    }

    public void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    LobbyGUIController.this.secondsLabel.setText(String.valueOf(remainingTime));
                    remainingTime--;
                });
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        this.timer.cancel();
    }
}
