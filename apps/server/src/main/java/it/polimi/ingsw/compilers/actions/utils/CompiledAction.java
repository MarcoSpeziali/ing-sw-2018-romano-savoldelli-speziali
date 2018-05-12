package it.polimi.ingsw.compilers.actions.utils;

import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ExecutableAction;

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
    @SuppressWarnings("squid:S1948")
    private ActionData actionData;


    /**
     * The list of parameters needed to initialize the action.
     */
    private List<ActionParameter> parameters;

    /**
     * Represent the need of the action for an user interaction.
     */
    private Boolean requiresUserInteraction;

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
    public List<ActionParameter> getParameters() {
        return parameters;
    }

    /**
     * @return the need of the action for an user interaction
     */
    public Boolean requiresUserInteraction() {
        return requiresUserInteraction;
    }

    /**
     * @param actionId the action id
     * @param actionClass the class of the action
     * @param actionData the data of the action
     * @param parameters the parameters needed to initialize the action
     * @param requiresUserInteraction the need of the action for an user interaction
     */
    public CompiledAction(String actionId, Class<? extends ExecutableAction> actionClass, ActionData actionData, List<ActionParameter> parameters, Boolean requiresUserInteraction) {
        this.actionId = actionId;
        this.actionClass = actionClass;
        this.actionData = actionData;
        this.parameters = parameters;
        this.requiresUserInteraction = requiresUserInteraction;
    }
}
