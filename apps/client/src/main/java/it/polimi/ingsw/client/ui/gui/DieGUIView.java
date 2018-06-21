package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.DieView;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class DieGUIView extends DieView {

    @FXML
    public ImageView imageView;
    @FXML
    public AnchorPane anchorPane;

    public DieGUIView() {
    }

    @Override
    public void setDie(Die die) {
        super.setDie(die);

        anchorPane.setStyle("-fx-background-radius: 10;"+
                "-fx-background-color: #" + Integer.toHexString(this.die.getColor().getHex())
        );
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        anchorPane.setEffect(dropShadow);

        String resourceName = String.format("DIE_%d", super.die.getShade());
        String relativePath = Constants.Resources.valueOf(resourceName).getRelativePath();
        URL resourceUrl = Resources.getResource(DieGUIView.class.getClassLoader(), relativePath);

        if (resourceUrl != null) {
            try {
                imageView.setImage(new Image(resourceUrl.openStream()));
            }
            catch (IOException e) {
                ClientLogger.getLogger().log(Level.SEVERE, "IOException while reading image at path: " + relativePath, e);
            }
        }
        else {
            ClientLogger.getLogger().log(Level.WARNING, "Could not retrieve image for resource {0}", resourceName);
        }
    }
}
