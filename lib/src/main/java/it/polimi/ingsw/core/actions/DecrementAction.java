package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

public class DecrementAction extends Action {

    protected final Die die;
    protected final Integer by;

    public DecrementAction(ActionData data, Die die, Integer by) {
        super(data);

        this.die = die;
        this.by = by;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        Integer result = this.die.getShade() - this.by;

        this.die.setShade(result <= 0 ? result + 6 : result);
        return this.die;
    }
}
