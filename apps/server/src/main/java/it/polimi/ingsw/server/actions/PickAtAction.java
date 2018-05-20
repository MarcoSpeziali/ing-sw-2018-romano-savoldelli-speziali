package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;

// TODO: docs
public class PickAtAction extends Action {

    private static final long serialVersionUID = -7907295780644054193L;
    private final VariableSupplier<ChoosablePickLocation> from;
    private final VariableSupplier<Integer> at;

    public PickAtAction(ActionData data, VariableSupplier<ChoosablePickLocation> from, VariableSupplier<Integer> at) {
        super(data);

        this.from = from;
        this.at = at;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.get(context).pickDie(this.at.get(context));
    }
}
