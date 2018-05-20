package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.locations.RandomPutLocation;

// TODO: docs
public class PutAction extends Action {

    private static final long serialVersionUID = 6807507670869610757L;
    private final VariableSupplier<Die> die;
    private final VariableSupplier<RandomPutLocation> location;

    public PutAction(ActionData data, VariableSupplier<Die> die, VariableSupplier<RandomPutLocation> location) {
        super(data);

        this.die = die;
        this.location = location;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        this.location.get(context).putDie(this.die.get(context));
        return null;
    }
}
