package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.RandomPutLocation;

public class PutAction extends Action {

    private final Die die;
    private final RandomPutLocation location;

    public PutAction(ActionData data, Die die, RandomPutLocation location) {
        super(data);

        this.die = die;
        this.location = location;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        this.location.putDie(this.die);
        return null;
    }
}
