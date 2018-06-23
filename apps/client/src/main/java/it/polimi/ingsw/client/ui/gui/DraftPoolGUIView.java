package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.DraftPool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DraftPoolGUIView extends GUIView {

    public DraftPoolGUIView() {
    }

    @FXML
    public AnchorPane anchorPane;


    public void onUpdateReceived(DraftPool draftPool){
    }

    //@Override
    public void setDraftPool(DraftPool draftPool) throws IOException {
        //super.setDraftPool(draftPool);
        for (int i = 0; i < draftPool.getNumberOfDice(); i++) {
            AnchorPane placeholder = (AnchorPane) anchorPane.getChildren().get(i);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            AnchorPane die = loader.load();
            DieGUIView controller = loader.getController();
            //controller.setDie(draftPool.getDice().get(i));
            placeholder.getChildren().add(die);
        }
    }

    @Override
    public void init() {

    }
}

