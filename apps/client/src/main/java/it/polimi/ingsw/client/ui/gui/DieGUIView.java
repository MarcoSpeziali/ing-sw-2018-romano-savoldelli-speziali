package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.DieView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;

import java.io.IOException;

public class DieGUIView extends DieView {

    private String path;
    
    public DieGUIView() {
    }

    @FXML
    public ImageView imageView;

    @FXML
    public AnchorPane anchorPane;

    @Override
    public void setDie(Die die) {

        super.setDie(die);
        anchorPane.setStyle(
                "-fx-background-color: #" + Integer.toHexString(this.die.getColor().getHex())
        );
        switch (super.die.getShade()) {
            case 1:
                path = Constants.Resources.DICE_ONE.getRelativePath();
                break;
            case 2:
                path = Constants.Resources.DICE_TWO.getRelativePath();
                break;
            case 3:
                path = Constants.Resources.DICE_THREE.getRelativePath();
                break;
            case 4:
                path = Constants.Resources.DICE_FOUR.getRelativePath();
                break;
            case 5:
                path = Constants.Resources.DICE_FIVE.getRelativePath();
                break;
            case 6:
                path = Constants.Resources.DICE_SIX.getRelativePath();
                break;
        }
        try {
            imageView.setImage(new Image(Resources.getResource(CellGUIView.class.getClassLoader(), path).openStream()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        
    }
}
