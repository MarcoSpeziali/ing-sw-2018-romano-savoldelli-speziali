package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseDieAction extends Action {

    @SuppressWarnings("WeakerAccess")
    protected final UserInteractionProvider userInteractionProvider;
    protected final ChooseLocation from;
    protected final VariableSupplier<GlassColor> color;
    protected final VariableSupplier<Integer> shade;

    public ChooseDieAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, VariableSupplier<GlassColor> color, VariableSupplier<Integer> shade) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.color = color;
        this.shade = shade;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.chooseDie(this.from, this.color.get(context), this.shade.get(context));
    }
}
