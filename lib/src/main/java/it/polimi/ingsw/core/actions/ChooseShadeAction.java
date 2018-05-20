package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

// TODO: docs
public class ChooseShadeAction extends ChooseDieAction {
    private static final long serialVersionUID = 9099616213240607342L;

    public ChooseShadeAction(ActionData data, UserInteractionProvider userInteractionProvider, VariableSupplier<ChooseLocation> from, VariableSupplier<GlassColor> color) {
        super(data, userInteractionProvider, from, color, context -> 0);
    }

    @Override
    public Object run(Context context) {
        return ((Die) super.run(context)).getShade();
    }
}
