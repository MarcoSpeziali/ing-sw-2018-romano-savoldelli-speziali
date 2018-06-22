package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.RoundTrack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class RoundTrackGUIView {

    @FXML
    public GridPane gridPane;

    public RoundTrackGUIView(){
    }

    // TODO change implementation of setRoundtrack, should extend GUIView and use IRoundtrack
    public void setRoundTrack(RoundTrack roundTrack) throws IOException {
        for (int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            Parent dieView = loader.load();
            DieGUIView controller = loader.getController();
            if (roundTrack.getDiceForRound(i).size() <= 1) {
                controller.setDie(roundTrack.getDiceForRoundAtIndex(1,0));
            }
            else {
                controller.setDie(roundTrack.getDiceForRoundAtIndex(i, 1));
            }
            gridPane.add(dieView, i,0);

        }
        gridPane.setHgap(10);

    }

}
