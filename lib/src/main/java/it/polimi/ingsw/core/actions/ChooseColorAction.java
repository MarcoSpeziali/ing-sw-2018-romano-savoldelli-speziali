package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

// TODO: docs
public class ChooseColorAction extends ChooseDieAction {

    private static final long serialVersionUID = -1654067241403388125L;

    public ChooseColorAction(ActionData data, UserInteractionProvider userInteractionProvider, VariableSupplier<ChooseLocation> from, VariableSupplier<Integer> shade) {
        super(data, userInteractionProvider, from, context -> null, shade);
    }

    @Override
    public Object run(Context context) {
        return ((Die) super.run(context)).getColor();
    }
}
