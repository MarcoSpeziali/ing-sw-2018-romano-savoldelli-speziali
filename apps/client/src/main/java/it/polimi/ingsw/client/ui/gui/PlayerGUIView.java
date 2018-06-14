package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.views.PlayerView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PlayerGUIView extends PlayerView implements GUIView{

    @Override
    public Node render() {
        return new Pane();
    }
}
