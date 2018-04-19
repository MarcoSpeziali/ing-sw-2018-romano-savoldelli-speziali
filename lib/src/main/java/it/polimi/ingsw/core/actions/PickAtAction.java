package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;

public class PickAtAction extends Action {

    private final ChoosablePickLocation from;
    private final Integer at;

    public PickAtAction(ActionData data, ChoosablePickLocation from, Integer at) {
        super(data);

        this.from = from;
        this.at = at;
    }

    @Override
    public Object run(Context context) {
        return this.from.pickDie(this.at);
    }
}
