package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ExecutableAction;

import java.io.Serializable;

/**
 * Defines common methods to {@link CompiledAction} and {@link CompiledActionGroup}.
 */
public interface CompiledExecutableAction extends Serializable {
    /**
     * @return the action data
     */
    ActionData getActionData();

    /**
     * @return the class of the action
     */
    Class<? extends ExecutableAction> getClassToInstantiate();
}
