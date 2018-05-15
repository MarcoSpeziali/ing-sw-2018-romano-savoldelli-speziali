package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;

public class PickDieAction extends Action {

    private static final long serialVersionUID = 8262178826626003006L;
    private final ChoosablePickLocation from;
    private final VariableSupplier<Die> die;

    public PickDieAction(ActionData data, ChoosablePickLocation from, VariableSupplier<Die> die) {
        super(data);

        this.from = from;
        this.die = die;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.pickDie(this.die.get(context));
    }
}

