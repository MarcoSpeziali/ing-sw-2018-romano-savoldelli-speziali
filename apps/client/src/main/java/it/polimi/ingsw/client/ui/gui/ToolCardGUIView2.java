package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ToolCardView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

public class ToolCardGUIView2 extends ToolCardView {
    public ToolCardGUIView2(ToolCard toolCard) {
        super(toolCard);
    }

    public Node render() {
        ImageView view = new ImageView();
        AnchorPane root = new AnchorPane();
        Label draft = new Label();
        Label bag = new Label();
        Text title = new Text();
        Text effect = new Text();
        draft.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_DRAFT));
        bag.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_BAG_TEXT));
        draft.setLayoutY(175);
        draft.setLayoutX(17);
        draft.setRotate(-35);
        bag.setRotate(35);
        bag.setLayoutX(140);
        bag.setLayoutY(180);
        title.setText(Constants.Strings.toLocalized(Constants.Strings.getToolCardTitle(toolCard.getCardId())));
        title.setLayoutX(45);
        title.setLayoutY(10);
        title.setWrappingWidth(150);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(new Font("Copperplate Gothic Bold",10));
        bag.setFont(new Font("Copperplate Gothic Bold",8));
        draft.setFont(new Font("Copperplate Gothic Bold",8));
        bag.setTextAlignment(TextAlignment.CENTER);
        draft.setTextAlignment(TextAlignment.CENTER);
        String path;
        effect.setText(Constants.Strings.toLocalized(Constants.Strings.getToolCardEffect(toolCard.getCardId())));
        effect.setLayoutX(5);
        effect.setLayoutY(220);
        effect.setWrappingWidth(190);
        effect.setFont(new Font("Copperplate Gothic Bold",10));
        effect.setTextAlignment(TextAlignment.JUSTIFY);
        path = Constants.Resources.valueOf(String.format("TOOL_CARD_%s", toolCard.getCardId().toUpperCase())).getRelativePath();
        view.setFitHeight(300);
        view.setFitWidth(200);
        try {
            view.setImage(new Image(Resources.getResource(ToolCardGUIView2.class.getClassLoader(), path).openStream()));
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        root.getChildren().add(view);
        root.getChildren().add(draft);
        root.getChildren().add(bag);
        root.getChildren().add(title);
        root.getChildren().add(effect);

        return root;
    }
}

