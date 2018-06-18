package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.ToolCardController;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ToolCardView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ToolCardGUIView extends ToolCardView implements GUIView {
    public ToolCardGUIView(ToolCard toolCard) {
        super(toolCard);
    }

    @Override
    public Node render() {
        ImageView view = new ImageView();
        AnchorPane root = new AnchorPane();
        Label draft = new Label();
        Label bag = new Label();
        draft.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_DRAFT));
        bag.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_BAG_TEXT));
        draft.setLayoutY(150);
        draft.setLayoutX(15);
        String path;
        path = Constants.Resources.valueOf(String.format("TOOL_CARD_%s", toolCard.getCardId().toUpperCase())).getRelativePath();
        view.setFitHeight(300);
        view.setFitWidth(200);
        try {
            view.setImage(new Image(Resources.getResource(ToolCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        root.getChildren().add(view);
        root.getChildren().add(draft);
        root.getChildren().add(bag);

        return root;
    }
}

