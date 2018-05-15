package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;

public class PickAtAction extends Action {

    private static final long serialVersionUID = -7907295780644054193L;
    private final ChoosablePickLocation from;
    private final VariableSupplier<Integer> at;

    public PickAtAction(ActionData data, ChoosablePickLocation from, VariableSupplier<Integer> at) {
        super(data);

        this.from = from;
        this.at = at;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.pickDie(this.at.get(context));
    }
}
