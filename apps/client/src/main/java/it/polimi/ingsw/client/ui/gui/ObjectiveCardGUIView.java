package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.ObjectiveCardController;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ObjectiveCardView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ObjectiveCardGUIView extends ObjectiveCardView  {
    public ObjectiveCardGUIView(ObjectiveCard objectiveCard){
        super(objectiveCard);
    }
    public void setObjectiveCardController(ObjectiveCardController objectiveCardController){
        super.setObjectiveCardController(objectiveCardController);
    }


    public Node render() {
        ImageView view = new ImageView();
        String path = Constants.Resources.valueOf(String.format("OBJECTIVE_CARD_%s", objectiveCard.getCardId().toUpperCase())).getRelativePath();
        view.setFitHeight(300);
        view.setFitWidth(200);
        try {
            view.setImage(new Image(Resources.getResource(ObjectiveCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }


        return view;
    }
}
