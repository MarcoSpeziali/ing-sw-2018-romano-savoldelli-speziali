package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.DieController;
import it.polimi.ingsw.models.Die;

public abstract class DieView {

    protected Die die;
    protected DieController dieController;

    public void setDieController(DieController dieController) {
        this.dieController = dieController;
    }
    public void setDie(Die die) {
        this.die = die;
    }

}
