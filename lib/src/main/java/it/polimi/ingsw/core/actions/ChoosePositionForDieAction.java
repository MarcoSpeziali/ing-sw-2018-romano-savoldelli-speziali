package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChoosePositionForDieAction extends Action {

    private final UserInteractionProvider userInteractionProvider;
    private final ChooseLocation from;
    private final Die die;

    public ChoosePositionForDieAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, Die die) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.die = die;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return this.userInteractionProvider.choosePosition(this.from, this.die);
    }
}
