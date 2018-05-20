package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class PickDieAction extends Action {

    private static final long serialVersionUID = 8262178826626003006L;
    private final VariableSupplier<ChoosablePickLocation> from;
    private final VariableSupplier<Die> die;

    public PickDieAction(ActionData data, VariableSupplier<ChoosablePickLocation> from, VariableSupplier<Die> die) {
        super(data);

        this.from = from;
        this.die = die;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.get(context).pickDie(this.die.get(context));
    }
}

