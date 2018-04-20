package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;

public class ChoosePositionForDieAction extends Action {

    private final UserInteractionProvider userInteractionProvider;
    private final RestrictedChoosablePutLocation from;
    private final Die die;
    private final Boolean ignoreColor;
    private final Boolean ignoreShade;
    private final Boolean ignoreAdjacency;

    public ChoosePositionForDieAction(ActionData data, UserInteractionProvider userInteractionProvider, RestrictedChoosablePutLocation from, Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
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
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return this.userInteractionProvider.choosePosition(this.from, this.die, this.ignoreColor, this.ignoreShade, this.ignoreAdjacency);
    }
}
