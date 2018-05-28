package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;

import java.io.Serializable;

public class DieController implements Serializable {
    private static final long serialVersionUID = 867107781400962437L;

    private Die dieModel;

    public void setDieModel(Die dieModel) {
        this.dieModel = dieModel;
    }

    public DieController(Die dieModel) {
        this.dieModel = dieModel;
    }

    public void setDieShade(int shade) {
        this.dieModel.setShade(shade);
    }
}
