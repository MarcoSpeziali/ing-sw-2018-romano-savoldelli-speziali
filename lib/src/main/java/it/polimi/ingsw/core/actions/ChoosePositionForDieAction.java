package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;

// TODO: docs
public class ChoosePositionForDieAction extends Action {

    private static final long serialVersionUID = -2003152924850538064L;

    private final UserInteractionProvider userInteractionProvider;
    private final VariableSupplier<RestrictedChoosablePutLocation> from;
    private final VariableSupplier<Die> die;
    private final Boolean ignoreColor;
    private final Boolean ignoreShade;
    private final Boolean ignoreAdjacency;

    public ChoosePositionForDieAction(ActionData data, UserInteractionProvider userInteractionProvider, VariableSupplier<RestrictedChoosablePutLocation> from, VariableSupplier<Die> die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.die = die;
        this.ignoreColor = ignoreColor;
        this.ignoreShade = ignoreShade;
        this.ignoreAdjacency = ignoreAdjacency;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.choosePosition(
                this.from.get(context),
                this.die.get(context),
                this.ignoreColor,
                this.ignoreShade,
                this.ignoreAdjacency
        );
    }
}
