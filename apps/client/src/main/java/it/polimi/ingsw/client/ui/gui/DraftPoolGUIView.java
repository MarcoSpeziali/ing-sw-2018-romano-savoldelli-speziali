package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.DraftPoolController;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.net.mocks.IDraftPool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DraftPoolGUIView extends GUIView<DraftPoolController> {

    public DraftPoolGUIView() {
    }

    @FXML
    public AnchorPane anchorPane;


    public void onUpdateReceived(DraftPool draftPool){
    }

    @Override
    public void setController(DraftPoolController draftPoolController) throws IOException {
        super.setController(draftPoolController);

        IDraftPool iDraftPool = draftPoolController.getDraftPool();

        for (int i = 0; i < iDraftPool.getNumberOfDice(); i++) {
            AnchorPane placeholder = (AnchorPane) anchorPane.getChildren().get(i);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            AnchorPane die = loader.load();
            DieGUIView dieGUIView = loader.getController();
            //dieGUIView.setController(draftPool.getDice().get(i)); TODO change controller of draftpool
            placeholder.getChildren().add(die);
        }
    }

    @Override
    public void init() {

    }

}

