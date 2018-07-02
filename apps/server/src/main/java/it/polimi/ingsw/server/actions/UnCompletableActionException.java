package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.models.Die;

public class UnCompletableActionException extends RuntimeException {
    private static final long serialVersionUID = 3393515717423382855L;

    private final Die die;

    public Die getDie() {
        return die;
    }

    public UnCompletableActionException(Die die) {
        this.die = die;
    }
}
