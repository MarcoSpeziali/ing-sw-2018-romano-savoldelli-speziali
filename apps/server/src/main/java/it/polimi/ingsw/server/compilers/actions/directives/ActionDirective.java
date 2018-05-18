package it.polimi.ingsw.server.compilers.actions.directives;

import it.polimi.ingsw.server.compilers.commons.directives.ClassDirective;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.core.actions.ExecutableAction;

import java.io.Serializable;
import java.util.List;

/**
 * Defines the directives to instantiate an action.
 */
public class ActionDirective extends ClassDirective<ExecutableAction> implements Serializable {

    private static final long serialVersionUID = 2802217925796490744L;

    /**
     * The requirement of the action of user interaction.
     */
    private boolean requiresUserInteraction;

    /**
     * @return {@code true} if the action requires the user interaction, {@code false} otherwise
     */
    public boolean requiresUserInteraction() {
        return requiresUserInteraction;
    }

    /**
     * @param id                        the id of the action
     * @param targetClass               the class to instantiate to execute the action
     * @param requiresUserInteraction   {@code true} if the action requires the user interaction, {@code false} otherwise
     * @param parametersDirectives      the directives to instantiate the parameters
     */
    public ActionDirective(String id, Class<ExecutableAction> targetClass, boolean requiresUserInteraction, List<ParameterDirective> parametersDirectives) {
        super(id, targetClass, parametersDirectives);

        this.requiresUserInteraction = requiresUserInteraction;
    }
}
