package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

import java.util.Random;

public class RedrawAction extends Action {

    private final Die die;

    public RedrawAction(ActionData data, Die die) {
        super(data);

        this.die = die;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        Random random = new Random(System.currentTimeMillis());

        // random.nextInt(int bound) returns a number in the set [0, bound)
        this.die.setShade(1 + random.nextInt(6));
        return null;
    }
}
