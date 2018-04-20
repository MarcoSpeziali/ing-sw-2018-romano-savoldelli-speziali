package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.utils.Range;

import java.util.Set;

/**
 * A set of callbacks needed by {@code ActionGroup}.
 */
public interface ActionGroupCallbacks {

    /**
     * @param alreadyRepeatedFor The number of repetition already done.
     * @param maximumRepetitions The maximum number of repetitions.
     * @return If the action group should continue to iterate.
     */
    boolean shouldRepeat(int alreadyRepeatedFor, int maximumRepetitions);

    /**
     * @param actions The possible actions.
     * @param chooseBetween The number of possible actions to be chosen.
     * @return The chosen actions.
     */
    Set<ExecutableAction> getChosenActions(Set<ExecutableAction> actions, Range<Integer> chooseBetween);
}
