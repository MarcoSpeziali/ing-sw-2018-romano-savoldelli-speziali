package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;

public class DraftPoolController {

    private DraftPool draftPool;

    public DraftPoolController(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public void setDraftPoolModel(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public Die onDiePicked(Die die) {
        return this.draftPool.pickDie(die);
    }

    public Die onDiePicked(int location) {
        return this.draftPool.pickDie(location);
    }

    public void onDiePut(Die die) {
        this.draftPool.putDie(die);
    }

    /*public void onDieChosen(Die die, Player player) {
        player.setPickedDie(die);
        draftPool.pickDie(die);
    }

    public void onDieRejected(Player player) {
        Die  die = player.getPickedDie();
        player.setPickedDie(null);
        draftPool.putDie(die);
    }
    */ //TODO manca la classe player
}
