package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

import java.util.Set;

public class PickAction extends Action {

    protected final UserInteractionProvider userInteractionProvider;
    protected final PickLocation from;
    protected final Integer quantity;
    protected final GlassColor color;
    protected final Integer shade;

    public PickAction(ActionData data, UserInteractionProvider userInteractionProvider, PickLocation from, Integer quantity, GlassColor color, Integer shade) {
        super(data);

        this.from = from;
        this.quantity = quantity;
        this.color = color;
        this.shade = shade;
        this.userInteractionProvider = userInteractionProvider;
    }

    @Override
    public Object run(Context context) throws ConstraintEvaluationException {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        if (this.from instanceof ChoosablePickLocation) {
            ChoosablePickLocation cpl = (ChoosablePickLocation) this.from;

            Set<Die> chosenDice = this.userInteractionProvider.chooseDice(cpl, this.quantity, this.color, this.shade);
            return this.userInteractionProvider.pickDice(chosenDice, this.quantity, this.color, this.shade);
        }
        else {
            return this.from.getDice(this.quantity, this.color, this.shade);
        }
    }
}
