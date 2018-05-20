package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class SetValAction extends Action {

    private static final long serialVersionUID = -2314218736549779947L;
    private final VariableSupplier<Die> die;

    public SetValAction(ActionData data, VariableSupplier<Die> die) {
        super(data);

        this.die = die;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        this.die.get(context).setShade(this.userInteractionProvider.chooseShade(this.die.get(context)));
        return null;
    }
}
