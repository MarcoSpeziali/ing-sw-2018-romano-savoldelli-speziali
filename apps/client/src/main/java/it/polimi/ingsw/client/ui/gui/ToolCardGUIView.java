package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.ToolCardController;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ToolCardView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ToolCardGUIView extends ToolCardView implements GUIView {
    public ToolCardGUIView(ToolCard toolCard) {
        super(toolCard);
    }

    @Override
    public void setToolCardController(ToolCardController toolCardController) {
        super.setToolCardController(toolCardController);
    }

    @Override
    public Node render() {
        ImageView view = new ImageView();
        String path;
        path = Constants.Resources.TOOL_CARD_FLUX_BRUSH.getRelativePath();
        view.setFitHeight(381);
        view.setFitWidth(273);
        try {
            view.setImage(new Image(Resources.getResource(ToolCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }


        return view;
    }
}

