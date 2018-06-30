package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IRoundTrack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Map;

public class RoundTrackGUIView extends GUIView<IRoundTrack> {

    @FXML
    public GridPane gridPane;

    public RoundTrackGUIView(){
    }

    @Override
    public void setModel(IRoundTrack iRoundTrack) throws IOException {

        Map<Integer, IDie> map = iRoundTrack.getLocationDieMap();

        for (int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            Parent dieView = loader.load();
            DieGUIView controller = loader.getController();

            gridPane.add(dieView, i,0);
        }
        gridPane.setHgap(10);

    }

    @Override
    public void init() {

    }

}
