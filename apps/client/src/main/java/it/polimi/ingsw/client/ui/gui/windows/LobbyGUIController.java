package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.net.Lobby;
import it.polimi.ingsw.core.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyGUIController implements Initializable {
    private FXMLLoader loader = new FXMLLoader();
    private Lobby model;
    private Timeline timeline;

    private Integer timeSeconds;

    @FXML public JFXListView<Label> listView;
    @FXML public Label seconds;
    @FXML public JFXButton startMatchButton;


    public LobbyGUIController() {
        //this.startTimeline(20);
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
    public void startTimeline(int amount) { // TODO set a static server timer and an asynchronous "live cycle" of the class
        timeSeconds = amount;
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
                        if (model.getPlayers().size()>=2) startMatchButton.setDisable(false);
                        timeline.stop();
                    }
                }));
        timeline.playFromStart();
    }

    public void setNames() {
        //listView.setItems(this.model.getObservableList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.startTimeline(20);
        for (int i = 0; i <4 ; i++) {
            try {
                Label lbl = new Label("Item "+i);
                lbl.setMinHeight(100);

                listView.getItems().add(lbl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
