package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IRoundTrack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class RoundTrackGUIView extends GUIView<IRoundTrack> {

    public boolean isChosen() {
        return chosen;
    }

    private boolean chosen = false;

    @FXML
    public GridPane gridPane;

    private Constants.Status Status;

    public void setStatus(Constants.Status status) {
        this.Status = status;
    }

    public RoundTrackGUIView(){
    }



    @Override
    public void setModel(IRoundTrack iRoundTrack) throws IOException {
        int round=0;
        Map<Integer, IDie> locationDieMap = iRoundTrack.getLocationDieMap();

        HashMap<Byte, Byte> roundToDieIndexMap = locationDieMap.keySet().stream()
                .map(location -> Map.entry(
                        (byte) ((location & 0x0000FF00) >> 8),
                        (byte) (location & 0x000000FF))
                ).collect(Collector.of(
                        (Supplier<HashMap<Byte, Byte>>) HashMap::new,
                        (map, entry) -> {
                            map.computeIfPresent(
                                    entry.getKey(),
                                    (key, value) -> value < entry.getValue() ? entry.getValue() : value
                            );

                            map.computeIfAbsent(entry.getKey(), key -> entry.getValue());
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        },
                        map -> map,
                        Collector.Characteristics.IDENTITY_FINISH
                ));

        for(Map.Entry<Byte, Byte> entry : roundToDieIndexMap.entrySet()){
            byte roundIndex = (byte) ((entry.getKey()) & 0x000000FF);
            byte dieIndex = (byte) (entry.getValue() & 0x000000FF);
            int location = (roundIndex << 8 & 0x0000FF00) | (dieIndex & 0x000000FF);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            Node die = loader.load();


            die.setOnMousePressed(event -> {
                if (Status == Constants.Status.SELECTION_UNLOCKED) {
                    try {
                        die.setCursor(Cursor.HAND);
                        chosen = true;
                        Match.getMatchController().postChosenPosition(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            DieGUIView dieGUIView = loader.getController();
            dieGUIView.setModel(locationDieMap.get(location));
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: white;"+"-fx-text-fill: #2c3e50;"+"-fx-font-size: 10");
            Label label = new Label("Round " + (roundIndex+1));
            vBox.getChildren().add(label);
            vBox.getChildren().add(die);
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.TOP_CENTER);
            gridPane.add(vBox, roundIndex, 0);
            round++;
        }

        gridPane.setVgap(7);
        gridPane.setHgap(7);
        gridPane.setGridLinesVisible(false);

        for (int i = round; i < 10; i++) {
            VBox vBox = new VBox();
            Label label = new Label("" + (i+1));
            label.setStyle("-fx-text-fill: white;"+"-fx-font-size: 40");
            vBox.getChildren().add(label);
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.CENTER);
            gridPane.add(vBox, i, 0);

        }


    }



    @Override
    public void init() {

    }

}
