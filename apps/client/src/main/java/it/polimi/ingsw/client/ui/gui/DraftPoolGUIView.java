package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DraftPoolView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DraftPoolGUIView extends DraftPoolView implements GUIView {


    public DraftPoolGUIView(DraftPool draftPool) {
        super(draftPool);
    }

    public Node render() {
        return new Pane();
    }
}

