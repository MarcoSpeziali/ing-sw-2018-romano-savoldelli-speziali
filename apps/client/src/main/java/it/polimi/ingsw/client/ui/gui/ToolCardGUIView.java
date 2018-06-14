package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.views.ToolCardView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ToolCardGUIView extends ToolCardView implements GUIView {

    @Override
    public Node render() {
        return new Pane();
    }
}
