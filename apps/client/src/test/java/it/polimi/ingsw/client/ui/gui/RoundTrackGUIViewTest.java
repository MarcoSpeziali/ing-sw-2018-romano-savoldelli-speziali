package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.RoundTrackGUIView;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.controllers.RoundTrackController;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTrackGUIViewTest extends Application {

    public RoundTrackGUIViewTest(){}

    private RoundTrackGUIView roundTrackGUIView;
    private RoundTrackController roundTrackController;
    private Die die1 = new Die(5, GlassColor.RED);
    private Die die2 = new Die(1, GlassColor.GREEN);
    private Die die3 = new Die(2, GlassColor.PURPLE);
    private Die die4 = new Die(2,GlassColor.BLUE);
    private Die die5 = new Die(5, GlassColor.GREEN);
    private Die die6 = new Die(1, GlassColor.YELLOW);
    private Die die7 = new Die(6, GlassColor.BLUE);
    private Die die8 = new Die (3, GlassColor.PURPLE);
    private Die die9 = new Die(4,GlassColor.RED);
    private Die die10 = new Die(5, GlassColor.YELLOW);
    private Die die11 = new Die(2,GlassColor.PURPLE);
    private RoundTrack roundTrack = new RoundTrack();

    public void setUp(){
        roundTrack.addDieForRound(die1, 0);
        roundTrack.addDieForRound(die2, 1);
        roundTrack.addDieForRound(die3, 2);
        roundTrack.addDieForRound(die4, 3);
        roundTrack.addDieForRound(die5, 4);
        roundTrack.addDieForRound(die6, 5);
        roundTrack.addDieForRound(die7, 5);
        roundTrack.addDieForRound(die8, 6);
        roundTrack.addDieForRound(die9, 7);
        roundTrack.addDieForRound(die10,8);
        roundTrack.addDieForRound(die10, 9);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Constants.Resources.ROUNDTRACK_VIEW.getURL());
        Parent root = fxmlLoader.load();
        roundTrackGUIView = fxmlLoader.getController();
        //setUp();
        roundTrackGUIView.setRoundTrack(roundTrack);
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}