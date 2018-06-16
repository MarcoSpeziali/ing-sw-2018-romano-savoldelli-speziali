package it.polimi.ingsw.server.compilers.actions.directives;

import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.compilers.commons.directives.ClassDirective;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;

import java.io.Serializable;
import java.util.List;

/**
 * Defines the directives to instantiate an action.
 */
public class ActionDirective extends ClassDirective<ExecutableAction> implements Serializable {

    private static final long serialVersionUID = 2802217925796490744L;

    /**
     * @param id                   the id of the action
     * @param targetClass          the class to instantiate to execute the action
     * @param parametersDirectives the directives to instantiate the parameters
     */
    public ActionDirective(String id, Class<ExecutableAction> targetClass, List<ParameterDirective> parametersDirectives) {
        super(id, targetClass, parametersDirectives);
    }
}
