package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.core.actions.ActionData;

public class CompiledAction {
    private String actionId;
    private Class<? extends Action> actionClass;
    private ActionData actionData;
    private ActionParameter[] parameters;
    private Boolean requiresUserInteraction;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Class<? extends Action> getActionClass() {
        return actionClass;
    }

    public void setActionClass(Class<? extends Action> actionClass) {
        this.actionClass = actionClass;
    }

    public ActionData getActionData() {
        return actionData;
    }

    public void setActionData(ActionData actionData) {
        this.actionData = actionData;
    }

    public ActionParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(ActionParameter[] parameters) {
        this.parameters = parameters;
    }

    public Boolean getRequiresUserInteraction() {
        return requiresUserInteraction;
    }

    public void setRequiresUserInteraction(Boolean requiresUserInteraction) {
        this.requiresUserInteraction = requiresUserInteraction;
    }
}
