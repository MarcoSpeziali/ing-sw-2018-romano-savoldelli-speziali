package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.views.DieView;
import javafx.scene.image.ImageView;

public class DieGUIView extends DieView{

    private ImageView imageView;

    @Override
    public void setDie(Die die){
        super.setDie(die);
        imageView.setStyle(
                "-fx-background-color: #" + Integer.toHexString(this.die.getColor().getHex())
        );

    }

    public DieGUIView(ImageView imageView) {
        this.imageView = imageView;
    }


    public void render() {

    }
}
