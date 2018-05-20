package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class ChoosePositionAction extends Action {

    private static final long serialVersionUID = 2291839640072604945L;

    private final VariableSupplier<ChooseLocation> from;

    public ChoosePositionAction(ActionData data, VariableSupplier<ChooseLocation> from) {
        super(data);

        this.from = from;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.choosePosition(this.from.get(context));
    }
}
