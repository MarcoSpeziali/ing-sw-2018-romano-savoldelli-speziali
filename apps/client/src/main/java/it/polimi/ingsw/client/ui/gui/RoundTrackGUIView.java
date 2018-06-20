package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;
import it.polimi.ingsw.views.RoundTrackView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class RoundTrackGUIView extends RoundTrackView {

    @FXML
    public GridPane gridPane;

    FXMLLoader loader = new FXMLLoader();

    public RoundTrackGUIView(){

    }
    @Override
    public void setRoundTrack(RoundTrack roundTrack) throws IOException {
        super.setRoundTrack(roundTrack);
        for (int i = 0; i < 10; i++) {
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
        gridPane.setVgap(10);

    }

}
