package it.polimi.ingsw.compilers.actions.directives;

import it.polimi.ingsw.core.actions.Action;

import java.io.Serializable;
import java.util.List;

/**
 * Defines the directives to instantiate an action.
 */
public class ActionDirectives implements Serializable {

    /**
     * The id of the action.
     */
    private String id;

    /**
     * The class to instantiate to execute the action.
     */
    private Class<Action> targetClass;

    /**
     * The requirement of the action of user interaction.
     */
    private boolean requiresUserInteraction;

    /**
     * The directives to instantiate the constructor parameters.
     */
    private List<ActionParameterDirectives> parametersDirectives;

    /**
     * @return the id of the action
     */
    public String getId() {
        return id;
    }

    /**
     * @return the class to instantiate to execute the action
     */
    public Class<Action> getTargetClass() {
        return targetClass;
    }

    /**
     * @return {@code true} if the action requires the user interaction, {@code false} otherwise
     */
    public boolean requiresUserInteraction() {
        return requiresUserInteraction;
    }

    /**
     * @return the directives to instantiate the constructor parameters
     */
    public List<ActionParameterDirectives> getParametersDirectives() {
        return parametersDirectives;
    }

    /**
     * @param id the id of the action
     * @param targetClass the class to instantiate to execute the action
     * @param requiresUserInteraction {@code true} if the action requires the user interaction, {@code false} otherwise
     * @param parametersDirectives the directives to instantiate the constructor parameters
     */
    public ActionDirectives(String id, Class<Action> targetClass, boolean requiresUserInteraction, List<ActionParameterDirectives> parametersDirectives) {
        this.id = id;
        this.targetClass = targetClass;
        this.requiresUserInteraction = requiresUserInteraction;
        this.parametersDirectives = parametersDirectives;
    }
}
