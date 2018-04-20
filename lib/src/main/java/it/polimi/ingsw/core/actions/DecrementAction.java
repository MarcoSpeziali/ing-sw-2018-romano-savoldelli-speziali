package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.utils.MathUtils;

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
        int modularResult = MathUtils.modular(result, 6);

        this.die.setShade(modularResult == 0 ? 6 : modularResult);
        return null;
    }
}
