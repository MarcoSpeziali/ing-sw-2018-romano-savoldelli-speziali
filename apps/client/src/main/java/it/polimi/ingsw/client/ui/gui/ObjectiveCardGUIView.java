package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.views.ObjectiveCardView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ObjectiveCardGUIView extends ObjectiveCardView implements GUIView {

    @Override
    public Node render() {
        return new Pane();
    }
}
