package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.ObjectiveCardController;
import it.polimi.ingsw.net.mocks.IObjectiveCard;
import it.polimi.ingsw.utils.io.Resources;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class ObjectiveCardGUIView extends GUIView<ObjectiveCardController> {
    @FXML
    public ImageView imageView;
    @FXML
    public Text title;
    @FXML
    public Text description;

    public ObjectiveCardGUIView() {
    }


    public void setController(ObjectiveCardController objectiveCardController) throws IOException {
        super.setController(objectiveCardController);
        IObjectiveCard iObjectiveCard = objectiveCardController.getObjectiveCard();
        title.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardTitle(iObjectiveCard.getId())));
        description.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardDescription(iObjectiveCard.getId())));
        String path = Constants.Resources.valueOf(String.format("OBJECTIVE_CARD_%s", iObjectiveCard.getId().toUpperCase())).getRelativePath();
        try {
            imageView.setImage(new Image(Resources.getResource(ObjectiveCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }
}

