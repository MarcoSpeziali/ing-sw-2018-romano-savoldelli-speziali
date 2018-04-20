package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseColorAction extends ChooseDieAction {

    public ChooseColorAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, Integer shade) {
        super(data, userInteractionProvider, from, null, shade);
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return ((Die) super.run(context)).getColor();
    }
}
