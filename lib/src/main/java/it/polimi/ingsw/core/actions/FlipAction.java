package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;

public class FlipAction extends Action {

    private static final long serialVersionUID = 3304536240465898877L;
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
