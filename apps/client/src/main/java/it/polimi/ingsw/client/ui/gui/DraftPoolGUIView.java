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
                    if (status == Constants.Status.OWNER_UNLOCKED && (Match.performedAction & 0b01) != 0b01) {
                        Move.build().begin(location);

                        Dragboard db = finalSource.startDragAndDrop(TransferMode.ANY);
                        ClipboardContent content = new ClipboardContent();
                        content.put(Constants.iDieFormat, locationsDieMap.get(location));
                        db.setContent(content);
                        event.consume();
                    }
                });


                source.setOnMousePressed(event -> {
                    if (status == Constants.Status.SELECTION_UNLOCKED) {
                        try {
                            source.setCursor(Cursor.HAND);
                            Match.getMatchController().postChosenPosition(location);
                            Match.dialog.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
    }
}

