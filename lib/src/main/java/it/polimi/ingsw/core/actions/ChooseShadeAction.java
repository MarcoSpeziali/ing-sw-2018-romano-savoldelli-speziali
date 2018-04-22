package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseShadeAction extends ChooseDieAction {
    public ChooseShadeAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, VariableSupplier<GlassColor> color) {
        super(data, userInteractionProvider, from, color, context -> 0);
    }

    @Override
    public Object run(Context context) {
        return ((Die) super.run(context)).getShade();
    }
}
