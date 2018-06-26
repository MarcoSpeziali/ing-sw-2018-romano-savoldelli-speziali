package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.controllers.DieMockController;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.DraftPoolController;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IDraftPool;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.util.Map;

public class DraftPoolGUIView extends GUIView<DraftPoolController> {

    private double mouseX, mouseY;

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
        Map<Integer, IDie> locationsDieMap = iDraftPool.getLocationDieMap(); // FIXME: why using it?

        for (int i = 0; i < iDraftPool.getLocationDieMap().size(); i++) {
            AnchorPane placeholder = (AnchorPane) anchorPane.getChildren().get(i);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            AnchorPane die = loader.load();
            die.setCursor(Cursor.OPEN_HAND);
            Scale scale = new Scale(1.5,1.5);

            die.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                die.setCursor(Cursor.CLOSED_HAND);
                die.getTransforms().add(scale);
                die.setStyle("-fx-opacity: 0.5");
            });
            die.setOnMouseDragged(event-> {
                die.relocate(event.getSceneX() - mouseX, event.getScreenY() - mouseY);
            });
            die.setOnMouseReleased(event -> {
                die.setCursor(Cursor.OPEN_HAND);
                try {
                    die.getTransforms().add(scale.createInverse());
                    die.setStyle("-fx-opacity: 1.0");
                } catch (NonInvertibleTransformException e) {
                    e.printStackTrace();
                }
            });


            DieGUIView dieGUIView = loader.getController();
            dieGUIView.setController(new DieMockController(locationsDieMap.get(i))); // Don't know if usin index is correct
            placeholder.getChildren().add(die);
        }
    }

    @Override
    public void init() {

    }

}

