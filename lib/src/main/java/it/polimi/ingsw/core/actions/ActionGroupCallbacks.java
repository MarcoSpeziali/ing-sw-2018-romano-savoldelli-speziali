package it.polimi.ingsw.core.actions;

import java.util.Set;

public interface ActionGroupCallbacks {
    boolean shouldRepeat(int alreadyRepeatedFor, int maximumRepetitions);
    Set<ExecutableAction> getChosenActions(Set<ExecutableAction> actions);
}
