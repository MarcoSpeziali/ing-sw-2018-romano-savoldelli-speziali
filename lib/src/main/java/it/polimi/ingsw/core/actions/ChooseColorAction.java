package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseColorAction extends ChooseDieAction {

    public ChooseColorAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, VariableSupplier<Integer> shade) {
        super(data, userInteractionProvider, from, context -> null, shade);
    }

    @Override
    public Object run(Context context) {
        return ((Die) super.run(context)).getColor();
    }
}
