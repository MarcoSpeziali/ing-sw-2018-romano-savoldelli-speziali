package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.compilers.commons.CompiledParameter;

import java.io.Serializable;
import java.util.List;

/**
 * Holds the needed values to instantiate an action.
 */
public class CompiledAction implements CompiledExecutableAction, Serializable {

    private static final long serialVersionUID = 8903652974125106460L;

    /**
     * The id of the action (effect).
     */
    private String actionId;

    /**
     * The class of the action.
     */
    private Class<? extends ExecutableAction> actionClass;

    /**
     * The data of the action.
     */
    private ActionData actionData;

    /**
     * The list of parameters needed to initialize the action.
     */
    private List<CompiledParameter> parameters;

    /**
     * @param actionId    the action id
     * @param actionClass the class of the action
     * @param actionData  the data of the action
     * @param parameters  the parameters needed to initialize the action
     */
    public CompiledAction(String actionId, Class<? extends ExecutableAction> actionClass, ActionData actionData, List<CompiledParameter> parameters) {
        this.actionId = actionId;
        this.actionClass = actionClass;
        this.actionData = actionData;
        this.parameters = parameters;
    }

    /**
     * @return the action id
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * @return the action data
     */
    @Override
    public ActionData getActionData() {
        return actionData;
    }

    /**
     * @return the class of the action
     */

    @Override
    public Class<? extends ExecutableAction> getClassToInstantiate() {
        return actionClass;
    }

    /**
     * @return the parameters needed to initialize the action
     */
    public List<CompiledParameter> getParameters() {
        return parameters;
    }
}
