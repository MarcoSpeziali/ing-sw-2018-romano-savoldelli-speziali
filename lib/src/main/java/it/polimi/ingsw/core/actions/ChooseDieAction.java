package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChooseDieAction extends Action {

    protected final UserInteractionProvider userInteractionProvider;
    protected final ChooseLocation from;
    protected final GlassColor color;
    protected final Integer shade;

    public ChooseDieAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, GlassColor color, Integer shade) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.color = color;
        this.shade = shade;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return this.userInteractionProvider.chooseDie(this.from, this.color, this.shade);
    }
}
