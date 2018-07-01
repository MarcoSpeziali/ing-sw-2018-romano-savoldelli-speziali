package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.server.utils.VariableSupplier;

public class GetAtAction extends Action {

    private static final long serialVersionUID = 6227732907124412358L;

    private final VariableSupplier<ChoosablePickLocation> from;
    private final VariableSupplier<Integer> at;

    /**
     * Instantiates an {@code Action}.
     *
     * @param data The data which identifies the action.
     */
    public GetAtAction(ActionData data, VariableSupplier<ChoosablePickLocation> from, VariableSupplier<Integer> at) {
        super(data);
        this.from = from;
        this.at = at;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.from.get(context)
                .getDie(this.at.get(context));
    }
}
