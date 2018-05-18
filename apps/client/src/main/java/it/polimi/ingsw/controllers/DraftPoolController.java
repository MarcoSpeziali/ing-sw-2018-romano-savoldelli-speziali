package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.models.Player;

public class DraftPoolController {

    private DraftPool draftPool;

    public DraftPoolController(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public void onDieChosen(Die die, Player player) {

        player.setPickedDie(die);
        draftPool.pickDie(die);
    }

    public void onDieRejected(Player player) {
        Die  die = player.getPickedDie();
        player.setPickedDie(null);
        draftPool.putDie(die);
    }
}
