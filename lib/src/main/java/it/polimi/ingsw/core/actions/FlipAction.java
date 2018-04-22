package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;

import java.util.function.Supplier;

public class FlipAction extends Action {

    private final VariableSupplier<Die> die;

    public FlipAction(ActionData data, VariableSupplier<Die> die) {
        super(data);

        this.die = die;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        this.die.get(context).setShade(7 - this.die.get(context).getShade());
        return null;
    }
}
