package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

public class SwapAction extends Action {

    protected final Die die1;
    protected final Die die2;

    public SwapAction(ActionData data, Die die1, Die die2) {
        super(data);

        this.die1 = die1;
        this.die2 = die2;
    }

    @Override
    public Object run(Context context) throws ConstraintEvaluationException {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        GlassColor c1 = this.die1.getColor();
        Integer s1 = this.die1.getShade();

        this.die1.setColor(this.die2.getColor());
        this.die1.setShade(this.die2.getShade());

        this.die2.setColor(c1);
        this.die2.setShade(s1);

        return null;
    }
}
