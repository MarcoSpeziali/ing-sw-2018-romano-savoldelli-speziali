package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

import java.util.Set;

public class ChooseShadeAction extends ChooseDieAction {
    public ChooseShadeAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, Integer quantity, GlassColor color, Integer shade) {
        super(data, userInteractionProvider, from, quantity, color, shade);
    }

    @Override
    public Object run(Context context) throws ConstraintEvaluationException {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        //noinspection unchecked
        return ((Set<Die>) super.run(context)).stream()
                .map(Die::getShade)
                .toArray();
    }
}
