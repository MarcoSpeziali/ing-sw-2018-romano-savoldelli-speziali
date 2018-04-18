package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.constraints.EvaluableConstraint;

public abstract class Action implements ExecutableAction {
    protected ActionData data;

    @Override
    public String getId() {
        return this.data.getId();
    }

    @Override
    public String getNextActionId() {
        return this.data.getNextActionId();
    }

    @Override
    public EvaluableConstraint getActionConstraint() {
        return this.data.getConstraint();
    }

    public Action(ActionData data) {
        this.data = data;
    }
}
