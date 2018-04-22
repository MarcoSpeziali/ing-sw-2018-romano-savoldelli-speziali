package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.locations.RandomPutLocation;

public class PutAction extends Action {

    private final VariableSupplier<Die> die;
    private final RandomPutLocation location;

    public PutAction(ActionData data, VariableSupplier<Die> die, RandomPutLocation location) {
        super(data);

        this.die = die;
        this.location = location;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        this.location.putDie(this.die.get(context));
        return null;
    }
}
