package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.compilers.actions.utils.ActionParameter;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ExecutableAction;

// TODO: docs
public class CompiledAction implements CompiledExecutableAction {
    private String actionId;
    private Class<? extends ExecutableAction> actionClass;
    private ActionData actionData;
    private ActionParameter[] parameters;
    private Boolean requiresUserInteraction;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public ActionData getActionData() {
        return actionData;
    }

    public void setActionClass(Class<? extends ExecutableAction> actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public Class<? extends ExecutableAction> getClassToInstantiate() {
        return actionClass;
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
