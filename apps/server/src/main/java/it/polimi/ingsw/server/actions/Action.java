package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.server.constraints.ConstraintEvaluationException;

/**
 * Represents a single action which implements {@code ExecutableAction}
 */
public abstract class Action implements ExecutableAction {
    
    private static final long serialVersionUID = -3796338420482907445L;
    
    /**
     * The data of the action.
     */
    protected ActionData data;

    /**
     * The user interaction provider.
     */
    protected UserInteractionProvider userInteractionProvider;

    /**
     * Instantiates an {@code Action}.
     *
     * @param data The data which identifies the action.
     */
    public Action(ActionData data) {
        this.data = data;
    }

    @Override
    public ActionData getActionData() {
        return this.data;
    }

    /**
     * @return the user interaction provider
     */
    public UserInteractionProvider getUserInteractionProvider() {
        return this.userInteractionProvider;
    }

    /**
     * @param userInteractionProvider the new user interaction provider
     */
    public void setUserInteractionProvider(UserInteractionProvider userInteractionProvider) {
        this.userInteractionProvider = userInteractionProvider;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException(this.data.getConstraint().getId());
        }

        return null;
    }
}
