package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.views.CardView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CardGUIView extends CardView implements GUIView {

    @Override
    public Node render() {
        return new Pane();
    }
}
