package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChoosablePutLocation;

public class PlaceAction extends Action {

    private final Die die;
    private final ChoosablePutLocation location;
    private final Integer position;

    public PlaceAction(ActionData data, Die die, ChoosablePutLocation location, Integer position) {
        super(data);

        this.die = die;
        this.location = location;
        this.position = position;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        this.location.putDie(this.die, this.position);
        return null;
    }
}
