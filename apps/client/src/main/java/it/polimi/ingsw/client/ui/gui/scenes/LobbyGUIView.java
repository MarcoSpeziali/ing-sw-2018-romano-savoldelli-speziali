package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.proxies.socket.LobbySocketProxyController;
import it.polimi.ingsw.controllers.proxies.socket.MatchSocketProxyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.net.responses.MigrationResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.text.Localized;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class LobbyGUIView implements Initializable {

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

        unsafe(() -> {
            this.proxyController.init();
            setUpUpdateFuture();
            setUpMatchFuture();
        }).run();
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

    private void setUpUpdateFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.proxyController.waitForUpdate()))
                .thenAccept(this::onUpdateReceived);
    }

    private void setUpMatchFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.proxyController.waitForMigrationRequest()))
                .thenAccept(iMatch -> Platform.runLater(unsafe(() -> {
                    loader.setLocation(Constants.Resources.MATCH_FXML.getURL());
                    Parent parent = loader.load();

                    MatchController matchController;

                    if (Settings.getSettings().getProtocol() == Constants.Protocols.RMI) {
                        OneTimeRMIResponseProvider<MatchInterface> oneTimeRMIResponseProvider = new OneTimeRMIResponseProvider<>(
                                Settings.getSettings().getServerRMIAddress(),
                                Settings.getSettings().getServerRMIPort(),
                                MatchInterface.class
                        );

                        matchController = oneTimeRMIResponseProvider.getSyncRemoteObject(EndPointFunction.MATCH_MIGRATION_RMI, new Response<>(
                                new Header(
                                        SignInManager.getManager().getToken(),
                                        EndPointFunction.MATCH_MIGRATION
                                ),
                                new MigrationResponse(iMatch.getId())
                        ));
                    }
                    else {
                        matchController = new MatchSocketProxyController(
                                ((LobbySocketProxyController) this.proxyController).getPersistentSocketInteractionProvider(),
                                SignInManager.getManager().getToken()
                        );
                    }

                    matchController.init(iMatch.getId());

                    MatchGUIView matchGUIView = loader.getController();
                    matchGUIView.setController(matchController);

                    // MatchGUIView gameDashboardGUIController = loader.getController();
                    // gameDashboardGUIController...
                    SagradaGUI.showStage(parent, 1280, 720);
                })));
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

            setUpUpdateFuture();
        });
    }

    public void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    LobbyGUIView.this.secondsLabel.setText(String.valueOf(remainingTime));
                    remainingTime--;
                });
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        this.timer.cancel();
    }
}

