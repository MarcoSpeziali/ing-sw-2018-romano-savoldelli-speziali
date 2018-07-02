package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class ChoosePositionForDieAction extends Action {

    private static final long serialVersionUID = -2003152924850538064L;

    private final VariableSupplier<RestrictedChoosablePutLocation> from;
    private final VariableSupplier<Die> die;
    private final Boolean ignoreColor;
    private final Boolean ignoreShade;
    private final Boolean ignoreAdjacency;

    public ChoosePositionForDieAction(ActionData data, VariableSupplier<RestrictedChoosablePutLocation> from, VariableSupplier<Die> die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        super(data);

        this.from = from;
        this.die = die;
        this.ignoreColor = ignoreColor;
        this.ignoreShade = ignoreShade;
        this.ignoreAdjacency = ignoreAdjacency;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        Die dieInContext = this.die.get(context);

        Integer position = this.userInteractionProvider.choosePositionForDie(
                this.from.get(context),
                dieInContext,
                this.ignoreColor,
                this.ignoreShade,
                this.ignoreAdjacency
        );

        if (position == null) {
            throw new UnCompletableActionException(dieInContext);
        }

        return position;
    }
}
