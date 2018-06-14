package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.views.RoundTrackView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class RoundTrackGUIView extends RoundTrackView implements GUIView{

    @Override
    public Node render() {
        return new Pane();
    }
}
