package it.polimi.ingsw.client.ui.gui.windows;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class LobbyGUIController {
    private FXMLLoader loader = new FXMLLoader();
    private SignInManager model;
    private static Timeline timeline;

    private static final Integer AMOUNT = 20;
    private Integer timeSeconds;

    @FXML public Label player1;
    @FXML public Label player2;
    @FXML public Label player3;
    @FXML public Label player4;
    @FXML public Label seconds;


    public LobbyGUIController() {
        timeSeconds = AMOUNT;
    }

    public void onStartMatchClicked() {
        // TODO implement
    }

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL()); // TODO check relative path for controller in all FXMLs
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        SagradaGUI.primaryStage.show();
    }

    @FXML
    public void startTimeline() {
        seconds.setText(timeSeconds.toString());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeSeconds--;
                    if(timeSeconds<10) {
                        seconds.setText("0" + timeSeconds.toString());

                    }
                    else {
                        seconds.setText(timeSeconds.toString());
                    }
                    if(timeSeconds<=0){
                        timeline.stop();
                    }
                }));
        timeline.playFromStart();
    }

}
