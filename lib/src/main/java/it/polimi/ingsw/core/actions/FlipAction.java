package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

public class FlipAction extends Action {

    private final Die die;

    public FlipAction(ActionData data, Die die) {
        super(data);

        this.die = die;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        this.die.setShade(7 - this.die.getShade());

        return this.die;
    }
}
