package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IDraftPool;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Map;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class DraftPoolGUIView extends GUIView<IDraftPool> {

    public boolean isChosen() {
        return chosen;
    }

    private boolean chosen = false;

    private Constants.Property property;

    public void setProperty(Constants.Property property) {
        this.property = property;
    }

    public DraftPoolGUIView() {
    }

    @FXML
    public Pane pane;

    @Override
    public void setModel(IDraftPool iDraftPool) throws IOException {
        super.setModel(iDraftPool);

        Map<Integer, IDie> locationsDieMap = iDraftPool.getLocationDieMap();

        locationsDieMap.keySet().stream()
                .sorted().forEach(unsafe(location -> {
                    AnchorPane placeholder = (AnchorPane) pane.getChildren().get(location);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());

                    Node source = loader.load();
                    source.setCursor(Cursor.OPEN_HAND);

                    source.setOnDragDetected(event -> {
                        if (property == Constants.Property.OWNED) {
                            System.out.println("Drag detected");

                            Move move = Move.build();
                            move.begin(location);

                            Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                            ClipboardContent content = new ClipboardContent();
                            content.put(Constants.iDieFormat, locationsDieMap.get(location));
                            db.setContent(content);
                            event.consume();
                        }
                    });

                    source.setOnDragDone(new EventHandler<DragEvent>() {
                        @Override
                        public void handle(DragEvent event) {
                            if (event.getTransferMode() == TransferMode.MOVE) {
                                placeholder.getChildren().remove(source);
                            }
                        }
                    });

                    if (property == Constants.Property.SELECTION) {
                        source.setOnMousePressed(event -> {
                            try {
                                Match.getMatchController().postChosenPosition(location);
                                chosen = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }


                    DieGUIView dieGUIView = loader.getController();

                    dieGUIView.setModel(locationsDieMap.get(location));

                    placeholder.getChildren().add(source);
                }
        ));
    }



    @Override
    public void init() {

    }

}

