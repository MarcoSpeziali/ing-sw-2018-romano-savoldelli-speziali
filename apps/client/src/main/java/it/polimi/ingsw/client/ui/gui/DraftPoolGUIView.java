package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IDraftPool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DraftPoolGUIView extends GUIView<IDraftPool> {

    public boolean isChosen() {
        return chosen;
    }

    private boolean chosen = false;

    private Constants.Status status;

    private List<DieGUIView> dieGUIViewList = new LinkedList<>();

    public void setStatus(Constants.Status status) {
        this.status = status;
    }

    @FXML
    public Pane pane;

    @Override
    public void setModel(IDraftPool iDraftPool) throws IOException {
        super.setModel(iDraftPool);


        //Sembra che se venga rimosso non possa essere più aggiunto

        for (Node child : pane.getChildren()) {
            AnchorPane anchorPane = (AnchorPane) child;
            anchorPane.getChildren().removeAll();
        }

        Map<Integer, IDie> locationsDieMap = iDraftPool.getLocationDieMap();

        Map<Integer, IDie> dice = IntStream.range(0, iDraftPool.getMaxNumberOfDice())
                .mapToObj(index -> Map.entry(index, locationsDieMap.getOrDefault(index, new DieMock(0, GlassColor.RED))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        dice.forEach((location, die) -> {
            if (((AnchorPane) pane.getChildren().get(location)).getChildren().size() == 0) {
                AnchorPane placeholder = (AnchorPane) pane.getChildren().get(location);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());

                Node source;
                try {
                    source = loader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                source.setCursor(Cursor.OPEN_HAND);

                Node finalSource = source;
                source.setOnDragDetected(event -> {
                    if (status == Constants.Status.OWNER_UNLOCKED && (Match.performedAction & 1) != 1) {
                        Move.build().begin(location);

                        Dragboard db = finalSource.startDragAndDrop(TransferMode.ANY);
                        ClipboardContent content = new ClipboardContent();
                        content.put(Constants.iDieFormat, locationsDieMap.get(location));
                        db.setContent(content);
                        event.consume();
                    }
                });


                if (status == Constants.Status.SELECTION_UNLOCKED) {
                    source.setOnMousePressed(event -> {
                        Move.build().begin(location);
                        chosen = true;
                    });
                }

                DieGUIView dieGUIView = loader.getController();

                if (dieGUIViewList.size() > location) {
                    dieGUIViewList.set(location, dieGUIView);
                }
                else {
                    dieGUIViewList.add(dieGUIView);
                }
                placeholder.getChildren().add(source);
            }

            try {
                if (die.getShade() == 0) {
                    ((AnchorPane) pane.getChildren().get(location)).getChildren().remove(0);
                }
                else {
                    dieGUIViewList.get(location).setModel(die);
                }
            }
            catch (IOException e) {
                ClientLogger.getLogger().log(Level.SEVERE, String.format("Could not load resource for die (%s): ", die.toString()), e);
            }
        });

        /*
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

                                if (Status == Constants.Status.OWNER_UNLOCKED &
                                        (Match.performedAction & 1) != 1) {

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
                                        Match.performedAction = (byte) (1 | Match.performedAction);
                                    }
                                }
                            });

                            if (status == Constants.status.SELECTION_UNLOCKED) {
                                source.setOnMousePressed(event -> {
                                    // Match.getMatchController().postChosenPosition(location);
                                    Move.build().begin(location);
                                    chosen = true;
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
        */
    }
}

