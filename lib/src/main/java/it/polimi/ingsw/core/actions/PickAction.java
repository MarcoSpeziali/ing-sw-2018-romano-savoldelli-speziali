package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.RandomPickLocation;

// TODO: docs
public class PickAction extends Action {

    private static final long serialVersionUID = -9131822743611513860L;

    protected final VariableSupplier<RandomPickLocation> from;

    public PickAction(ActionData data, VariableSupplier<RandomPickLocation> from) {
        super(data);

        this.from = from;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.get(context).pickDie();
    }
}
