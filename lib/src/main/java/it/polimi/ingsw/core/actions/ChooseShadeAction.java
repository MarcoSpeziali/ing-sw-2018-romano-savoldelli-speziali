package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseShadeAction extends ChooseDieAction {
    public ChooseShadeAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, GlassColor color) {
        super(data, userInteractionProvider, from, color, 0);
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return ((Die) super.run(context)).getShade();
    }
}
