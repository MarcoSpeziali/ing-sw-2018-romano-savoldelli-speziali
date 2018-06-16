package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.views.BagView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class BagGUIView extends BagView implements GUIView {

    public BagGUIView(Bag bag) {
        super(bag);
    }

    @Override
    public Node render() {
        return new Pane();
    }
}
