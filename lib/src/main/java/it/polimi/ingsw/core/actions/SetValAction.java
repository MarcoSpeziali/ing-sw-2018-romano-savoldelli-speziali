package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

public class SetValAction extends Action {

    private final UserInteractionProvider userInteractionProvider;
    private final Die die;

    public SetValAction(ActionData data, UserInteractionProvider userInteractionProvider, Die die) {
        super(data);

        this.die = die;
        this.userInteractionProvider = userInteractionProvider;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        this.die.setShade(this.userInteractionProvider.chooseShade(this.die));
        return this.die;
    }
}
