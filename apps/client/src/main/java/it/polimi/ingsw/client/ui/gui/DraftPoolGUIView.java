package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
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

    private Constants.Status Status;

    public void setStatus(Constants.Status status) {
        this.Status = status;
    }

    public DraftPoolGUIView() {
    }

    @FXML
    public Pane pane;

    @Override
    public void setModel(IDraftPool iDraftPool) throws IOException {
        super.setModel(iDraftPool);

        Map<Integer, IDie> locationsDieMap = iDraftPool.getLocationDieMap();

        for (int i = 0; i < pane.getChildren().size(); i++) {

            int finalI = i;

            locationsDieMap.keySet().stream()
                    .sorted().forEach(location -> {

                        if (location == finalI && pane.getChildren().get(location) == null) {

                            AnchorPane placeholder = (AnchorPane) pane.getChildren().get(location);
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());

                            Node source = null;
                            try {
                                source = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            source.setCursor(Cursor.OPEN_HAND);

                            Node finalSource = source;
                            source.setOnDragDetected(event -> {
                                if (Status == Constants.Status.OWNER_UNLOCKED) {
                                    System.out.println("Drag detected");

                                    Move move = Move.build();
                                    move.begin(location);

                                    Dragboard db = finalSource.startDragAndDrop(TransferMode.ANY);
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
                                        placeholder.getChildren().remove(finalSource);
                                    }
                                }
                            });

                            if (Status == Constants.Status.SELECTION_UNLOCKED) {
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

                            try {
                                dieGUIView.setModel(locationsDieMap.get(location));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            placeholder.getChildren().add(source);
                        }
                        if (finalI != location) {
                            pane.getChildren().remove(finalI);
                        }

                    }
            );
        }
    }



    @Override
    public void init() {

    }

}

