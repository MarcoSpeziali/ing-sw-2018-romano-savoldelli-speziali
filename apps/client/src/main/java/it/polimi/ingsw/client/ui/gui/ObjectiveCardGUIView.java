package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ObjectiveCardView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

public class ObjectiveCardGUIView extends ObjectiveCardView {
    public ObjectiveCardGUIView(ObjectiveCard objectiveCard) {
        super(objectiveCard);
    }


    public Node render() {
        ImageView view = new ImageView();
        AnchorPane root = new AnchorPane();
        Text title = new Text();
        Text description = new Text();
        String path = Constants.Resources.valueOf(String.format("OBJECTIVE_CARD_%s", objectiveCard.getCardId().toUpperCase())).getRelativePath();
        title.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardTitle(objectiveCard.getCardId())));
        title.setX(67);
        title.setY(385);
        title.setFont(new Font("Copperplate Gothic Bold",12));
        title.setWrappingWidth(200);
        title.setTextAlignment(TextAlignment.CENTER);
        description.setText(Constants.Strings.toLocalized(Constants.Strings.getObjectiveCardDescription(objectiveCard.getCardId())));
        description.setFont(new Font("Copperplate Gothic Bold",11));
        description.setWrappingWidth(190);
        description.setY(400);
        description.setX(82);
        description.setTextAlignment(TextAlignment.JUSTIFY);
        view.setFitHeight(450);
        view.setFitWidth(300);

        try {
            view.setImage(new Image(Resources.getResource(ObjectiveCardGUIView.class.getClassLoader(), path).openStream()));
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        root.getChildren().add(view);
        root.getChildren().add(title);
        root.getChildren().add(description);


        return root;
    }
}
