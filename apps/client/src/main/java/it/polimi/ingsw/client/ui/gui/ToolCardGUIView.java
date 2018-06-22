package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.ToolCardView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class ToolCardGUIView extends ToolCardView {

   @FXML
   public ImageView imageView;
   @FXML
   public Text title;
   @FXML
   public Text effect;
   @FXML
   public Text draft;
   @FXML
   public Text bag;

   private String path;

    public ToolCardGUIView() {
    }

    public void setToolCard(ToolCard toolCard) {
        draft.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_DRAFT));
        bag.setText(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_BAG_TEXT));
        title.setText(Constants.Strings.toLocalized(Constants.Strings.getToolCardTitle(toolCard.getCardId())));
        effect.setText(Constants.Strings.toLocalized(Constants.Strings.getToolCardEffect(toolCard.getCardId())));
        path = Constants.Resources.valueOf(String.format("TOOL_CARD_%s", toolCard.getCardId().toUpperCase())).getRelativePath();
        try {
            imageView.setImage(new Image(Resources.getResource(ToolCardGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

