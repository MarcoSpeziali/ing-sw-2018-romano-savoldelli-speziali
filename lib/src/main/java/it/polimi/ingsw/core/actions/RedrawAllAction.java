package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

import java.util.Random;

public class RedrawAllAction extends Action {

    private final ChooseLocation location;

    public RedrawAllAction(ActionData data, ChooseLocation location) {
        super(data);

        this.location = location;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        Random random = new Random(System.currentTimeMillis());

        this.location.getDice().forEach(die -> die.setShade(1 + random.nextInt(6)));
        return null;
    }
}
