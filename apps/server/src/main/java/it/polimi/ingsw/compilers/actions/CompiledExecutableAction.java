package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ExecutableAction;

// TODO: docs
public interface CompiledExecutableAction {
    ActionData getActionData();

    Class<? extends ExecutableAction> getClassToInstantiate();
}
