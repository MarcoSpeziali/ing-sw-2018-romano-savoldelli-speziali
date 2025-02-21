package it.polimi.ingsw.server.compilers.actions;

import it.polimi.ingsw.server.actions.ActionData;
import it.polimi.ingsw.server.actions.ActionGroup;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.utils.IterableRange;

import java.io.Serializable;
import java.util.List;

/**
 * Holds the needed values to instantiate an action group.
 */
public class CompiledActionGroup implements CompiledExecutableAction, Serializable {

    private static final long serialVersionUID = 2637851216785706962L;

    /**
     * The data of the action.
     */
    private ActionData actionData;

    /**
     * The sub-actions.
     */
    private List<CompiledExecutableAction> actions;

    /**
     * The range of repetitions.
     */
    private IterableRange<Integer> repetitions;

    /**
     * The range of choose between.
     */
    private IterableRange<Integer> chooseBetween;

    public CompiledActionGroup(ActionData actionData, List<CompiledExecutableAction> actions, IterableRange<Integer> repetitions, IterableRange<Integer> chooseBetween) {
        this.actionData = actionData;
        this.actions = actions;
        this.repetitions = repetitions;
        this.chooseBetween = chooseBetween;
    }

    /**
     * @return the data of the action
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
        return ActionGroup.class;
    }

    /**
     * @return the sub-actions
     */
    public List<CompiledExecutableAction> getActions() {
        return actions;
    }

    /**
     * @return the range of repetitions
     */
    public IterableRange<Integer> getRepetitions() {
        return repetitions;
    }

    /**
     * @return the range of choose between
     */
    public IterableRange<Integer> getChooseBetween() {
        return chooseBetween;
    }
}
