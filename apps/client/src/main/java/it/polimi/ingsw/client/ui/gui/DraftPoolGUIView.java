package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.controllers.DieMockController;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.DraftPoolController;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IDraftPool;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class DraftPoolGUIView extends GUIView<DraftPoolController> {

    private double mouseX, mouseY;

    public DraftPoolGUIView() {
    }

    private Node selected;

    @FXML
    public AnchorPane anchorPane;


    public void onUpdateReceived(DraftPool draftPool){
    }

    @Override
    public void setController(DraftPoolController draftPoolController) throws IOException {
        super.setController(draftPoolController);

        IDraftPool iDraftPool = draftPoolController.getDraftPool();
        Map<Integer, IDie> locationsDieMap = iDraftPool.getLocationDieMap(); // FIXME: why using it?

        locationsDieMap.keySet().stream()
                .sorted().forEach(unsafe(key -> {
                    AnchorPane placeholder = (AnchorPane) anchorPane.getChildren().get(key);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
                    AnchorPane die = null;

                    die = loader.load();
                    die.setCursor(Cursor.OPEN_HAND);

                    AnchorPane finalDie = die;
                    die.setOnMousePressed(event -> {

                        mouseX = event.getSceneX();
                        mouseY = event.getSceneY();
                        Scale scale = new Scale(1.5, 1.5);
                        FXMLLoader innerLoader = new FXMLLoader();
                        innerLoader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
                        DieGUIView innerController = innerLoader.getController();
                        try {
                            selected = innerLoader.load();
                            innerController.setController(new DieMockController(locationsDieMap.get(key)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        selected.setLayoutX(finalDie.getLayoutX());
                        selected.setLayoutY(finalDie.getLayoutY());
                        selected.setCursor(Cursor.CLOSED_HAND);
                        selected.getTransforms().add(scale);
                        selected.setStyle("-fx-opacity: 0.5");
                        anchorPane.getChildren().add(selected);


                        //Match.getMatchController().PICKKKKK
                    });

                    die.setOnMouseDragged(event -> {
                        selected.relocate(event.getSceneX() - mouseX, event.getScreenY() - mouseY);
                    });
                    die.setOnMouseReleased(event -> {
                        selected.setCursor(Cursor.OPEN_HAND);
                        anchorPane.getChildren().remove(selected);
                    });

                    DieGUIView dieGUIView = loader.getController();

                    dieGUIView.setController(new DieMockController(locationsDieMap.get(key)));

                    placeholder.getChildren().add(die);
                }
        ));
    }

    @Override
    public void init() {

    }

}

