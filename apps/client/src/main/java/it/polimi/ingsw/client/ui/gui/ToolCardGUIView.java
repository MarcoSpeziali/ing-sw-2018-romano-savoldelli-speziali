package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.ToolCardController;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.views.ToolCardView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ToolCardGUIView extends ToolCardView implements GUIView {
    public ToolCardGUIView (ToolCard toolCard){
        super (toolCard);
    }

    @Override
    public void setToolCardController(ToolCardController toolCardController) {
        super.setToolCardController(toolCardController);
    }

    @Override
    public Node render() {
        ImageView view = new ImageView();
        view.setFitWidth(300);
        view.setFitHeight(600);
        String path;
        path = Constants.Resources.TOOL_CARD_FLUX_BRUSH.getRelativePath();
        Image img = new Image(path);
        view.setImage(img);



        return view;
    }

}
