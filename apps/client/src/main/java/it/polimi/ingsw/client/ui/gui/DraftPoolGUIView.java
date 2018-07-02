package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
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

    public Constants.Property property;

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

                    source.setOnDragDetected(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            if (property == Constants.Property.OWNED) {
                                Move move = Move.build();
                                move.begin(location);
                                /*try {
                                    Match.getMatchController().tryToMove(move);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
                                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                                ClipboardContent content = new ClipboardContent();
                                content.put(Constants.iDieFormat, locationsDieMap.get(location));
                                db.setContent(content);
                                event.consume();
                            }
                        }
                    });

                    source.setOnDragDone(new EventHandler<DragEvent>() {
                        @Override
                        public void handle(DragEvent event) {
                            placeholder.getChildren().remove(source);
                        }
                    });


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

