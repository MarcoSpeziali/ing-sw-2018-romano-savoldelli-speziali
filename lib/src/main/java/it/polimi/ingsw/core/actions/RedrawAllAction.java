package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

import java.util.Random;
import java.util.stream.Collectors;

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

        return this.location.getDice().stream()
                .peek(die -> die.setShade(1 + random.nextInt(6)))
                .collect(Collectors.toSet());
    }
}
