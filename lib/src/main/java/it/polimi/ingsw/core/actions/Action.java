package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;

/**
 * Represents a single action which implements {@code ExecutableAction}
 */
public abstract class Action implements ExecutableAction {

    /**
     * The data of the action.
     */
    protected ActionData data;

    @Override
    public ActionData getActionData() {
        return this.data;
    }

    /**
     * Instantiates an {@code Action}.
     * @param data The data which identifies the action.
     */
    public Action(ActionData data) {
        this.data = data;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException(this.data.getConstraint().getId());
        }

        return null;
    }
}
