package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

public class ChooseDieAction extends Action {

    protected final UserInteractionProvider userInteractionProvider;
    protected final ChooseLocation from;
    protected final Integer quantity;
    protected final GlassColor color;
    protected final Integer shade;

    public ChooseDieAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from, Integer quantity, GlassColor color, Integer shade) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
        this.quantity = quantity;
        this.color = color;
        this.shade = shade;
    }

    @Override
    public Object run(Context context) throws ConstraintEvaluationException {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        return this.userInteractionProvider.chooseDice(this.from, this.quantity, this.color, this.shade);
    }
}
