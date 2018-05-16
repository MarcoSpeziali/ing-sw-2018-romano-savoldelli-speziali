package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.DieView;

public class DieCLIView extends DieView {

    private GlassColor glassColor;
    private int shade;
    private int size;

    public DieCLIView(GlassColor glassColor, int shade, int size) {
        this.glassColor = glassColor;
        this.shade = shade;
        if (size % 2 != 0){
            this.size = size+1;
        }
        else this.size = size;
    }

    public void render() {
    }
}
