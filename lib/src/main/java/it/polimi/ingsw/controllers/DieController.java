package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;

public class DieController {

    private Die dieModel;

    public void setDieModel(Die dieModel) {
        this.dieModel = dieModel;
    }

    public DieController(Die dieModel) {
        this.dieModel = dieModel;
    }

    public void onDieInteraction(int shade) {
        this.dieModel.setShade(shade);
    }
}
