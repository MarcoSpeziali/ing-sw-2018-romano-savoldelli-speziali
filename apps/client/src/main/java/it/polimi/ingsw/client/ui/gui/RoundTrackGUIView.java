package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IRoundTrack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    @FXML
    public GridPane gridPane;


    public RoundTrackGUIView(){
    }

    @Override
    public void setModel(IRoundTrack iRoundTrack) throws IOException {
        Map<Integer, IDie> locationDieMap = iRoundTrack.getLocationDieMap();

        HashMap<Byte, Byte> roundToDieIndexMap = iRoundTrack.getLocationDieMap().keySet().stream()
                .map(location -> Map.entry(
                        (byte) ((location & 0x0000FF00) >> 8),
                        (byte) (location & 0x000000FF))
                ).collect(Collector.of(
                        (Supplier<HashMap<Byte, Byte>>) HashMap::new,
                        (map, entry) -> {
                            map.compute(
                                    entry.getKey(),
                                    (key, lastValue) -> {
                                        return lastValue != null && lastValue < entry.getValue() ?
                                                entry.getValue() :
                                                lastValue;
                                    }
                            );
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        },
                        map -> map,
                        Collector.Characteristics.IDENTITY_FINISH
                ));

        for(Map.Entry<Byte, Byte> entry : roundToDieIndexMap.entrySet()){
            byte roundIndex = (byte) ((entry.getKey()) & 0x0000FF00);
            byte dieIndex = (byte) (entry.getValue() & 0x000000FF);
            int location = (roundIndex << 8 & 0x0000FF00) | (dieIndex & 0x000000FF);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            Node die = loader.load();
            DieGUIView dieGUIView = loader.getController();
            dieGUIView.setModel(locationDieMap.get(location));
            VBox vBox = new VBox();
            Label label = new Label("Round " + (roundIndex+1));
            vBox.getChildren().add(label);
            vBox.getChildren().add(die);
            gridPane.add(vBox, roundIndex, 0);
        }
    }



    @Override
    public void init() {

    }

}
