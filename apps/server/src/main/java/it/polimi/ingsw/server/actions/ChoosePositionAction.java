package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class ChoosePositionAction extends Action {

    private static final long serialVersionUID = 2291839640072604945L;

    private final VariableSupplier<ChooseLocation> from;
    private final VariableSupplier<GlassColor> color;
    private final VariableSupplier<Integer> shade;

    public ChoosePositionAction(ActionData data, VariableSupplier<ChooseLocation> from, VariableSupplier<GlassColor> color, VariableSupplier<Integer> shade) {
        super(data);

        this.from = from;
        this.color = color;
        this.shade = shade;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.choosePosition(
                this.from.get(context),
                this.color.get(context),
                this.shade.get(context)
        );
    }
}
