package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

// TODO: docs
public class ChooseDieAction extends Action {

    private static final long serialVersionUID = 904381936728528141L;
    protected final UserInteractionProvider userInteractionProvider;
    protected final VariableSupplier<ChooseLocation> from;
    protected final VariableSupplier<GlassColor> color;
    protected final VariableSupplier<Integer> shade;

    public ChooseDieAction(ActionData data, UserInteractionProvider userInteractionProvider, VariableSupplier<ChooseLocation> from, VariableSupplier<GlassColor> color, VariableSupplier<Integer> shade) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.color = color;
        this.shade = shade;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.chooseDie(this.from.get(context), this.color.get(context), this.shade.get(context));
    }
}
