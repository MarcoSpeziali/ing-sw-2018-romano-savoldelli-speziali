package it.polimi.ingsw.core.actions;


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
}
