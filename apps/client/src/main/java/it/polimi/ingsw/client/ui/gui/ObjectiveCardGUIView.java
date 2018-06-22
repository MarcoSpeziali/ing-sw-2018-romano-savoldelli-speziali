package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.utils.io.Resources;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class ObjectiveCardGUIView extends GUIView {
    @FXML
    public ImageView imageView;
    @FXML
    public Text title;
    @FXML
    public Text description;

    public ObjectiveCardGUIView(ObjectiveCard objectiveCard){
    }


    public void setToolCard(ObjectiveCard objectiveCard) {
        title.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardTitle(objectiveCard.getCardId())));
        description.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardDescription(objectiveCard.getCardId())));
        String path = Constants.Resources.valueOf(String.format("OBJECTIVE_CARD_%s", objectiveCard.getCardId().toUpperCase())).getRelativePath();
        try {
            imageView.setImage(new Image(Resources.getResource(ObjectiveCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

