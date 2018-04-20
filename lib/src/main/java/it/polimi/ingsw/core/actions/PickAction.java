package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.RandomPickLocation;

public class PickAction extends Action {

    protected final RandomPickLocation from;

    public PickAction(ActionData data, RandomPickLocation from) {
        super(data);

        this.from = from;
    }

    @Override
    public Object run(Context context) {
        return this.from.pickDie();
    }
}
