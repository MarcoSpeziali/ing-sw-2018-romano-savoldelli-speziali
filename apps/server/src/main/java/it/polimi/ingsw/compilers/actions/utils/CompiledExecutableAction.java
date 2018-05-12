package it.polimi.ingsw.compilers.actions.utils;

import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ExecutableAction;

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
