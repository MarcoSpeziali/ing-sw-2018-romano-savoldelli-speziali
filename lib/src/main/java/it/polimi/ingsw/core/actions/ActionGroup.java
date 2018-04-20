package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.Range;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a group of actions.
 */
public class ActionGroup implements ExecutableAction {

    /**
     * The data of the action group.
     */
    private ActionData data;


    /**
     * A range of repetitions.
     */
    private IterableRange<Integer> repetitionNumber;


    /**
     * The number of actions to be chosen.
     */
    private Range<Integer> chooseBetween;

    /**
     * The actions to be executed.
     */
    private Set<ExecutableAction> actions;

    /**
     * The callbacks used to interact with the player.
     */
    private final ActionGroupCallbacks callbacks;

    @Override
    public ActionData getActionData() {
        return this.data;
    }

    /**
     * @return A range of repetitions.
     */
    public IterableRange<Integer> getRepetitionNumber() {
        return this.repetitionNumber;
    }

    /**
     * @return The number of actions to be chosen.
     */
    public Range<Integer> getChooseBetween() {
        return this.chooseBetween;
    }

    /**
     * @return The actions to be executed.
     */
    public Set<ExecutableAction> getActions() {
        return this.actions;
    }

    public ActionGroup(
            ActionData data,
            IterableRange<Integer> repetitionNumber,
            Range<Integer> chooseBetween,
            Set<ExecutableAction> actions,
            ActionGroupCallbacks callbacks) {
        this.data = data;
        this.repetitionNumber = repetitionNumber;
        this.chooseBetween = chooseBetween;
        this.actions = actions;
        this.callbacks = callbacks;
    }

    @Override
    public Object run(Context context) {
        if (this.data.getConstraint() != null && !this.data.getConstraint().evaluate(context)) {
            throw new ConstraintEvaluationException();
        }

        // Since the results get append one by one it's more efficient to use a LinkedList
        LinkedList<Object> results = new LinkedList<>();

        // If the range is 3..7 the first 3 iterations are mandatory
        for (int i = 0; i < this.repetitionNumber.getStart(); i++) {
            // If chooseBetween is null every actions needs to be executed
            if (this.chooseBetween == null) {
                results.addAll(
                        this.actions.stream()
                                .map(action -> action.run(context))
                                .collect(Collectors.toCollection(LinkedList::new))
                );
            }
            // Otherwise only a subset of them get executed
            else {
                results.addAll(
                        callbacks.getChosenActions(this.actions, this.chooseBetween).stream()
                                .map(action -> action.run(context))
                                .collect(Collectors.toCollection(LinkedList::new))
                );
            }
        }

        // The mandatory repetitions have already been executed, now the optional ones needs to be processed
        for (Integer index : this.repetitionNumber) {
            // Whenever shouldRepeat returns false the process stops
            if (!this.callbacks.shouldRepeat(
                    this.repetitionNumber.getStart() + index + 1,
                    this.repetitionNumber.getEnd())) {
                break;
            }

            // If chooseBetween is null every actions needs to be executed
            if (this.chooseBetween == null) {
                results.addAll(
                        this.actions.stream()
                                .map(action -> action.run(context))
                                .collect(Collectors.toCollection(LinkedList::new))
                );
            }
            // Otherwise only a subset of them get executed
            else {
                results.addAll(
                        callbacks.getChosenActions(this.actions, this.chooseBetween).stream()
                                .map(action -> action.run(context))
                                .collect(Collectors.toCollection(LinkedList::new))
                );
            }
        }

        return new HashSet<>(results);
    }
}
