package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.Range;

import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionGroup implements ExecutableAction {

    private ActionData data;

    private IterableRange<Integer> repetitionNumber;

    private Range<Integer> chooseBetween;

    private Set<ExecutableAction> actions;

    private final ActionGroupCallbacks callbacks;

    @Override
    public String getId() {
        return this.data.getId();
    }

    @Override
    public String getNextActionId() {
        return this.data.getNextActionId();
    }

    @Override
    public EvaluableConstraint getActionConstraint() {
        return this.data.getConstraint();
    }

    public IterableRange<Integer> getRepetitionNumber() {
        return this.repetitionNumber;
    }

    public Range<Integer> getChooseBetween() {
        return this.chooseBetween;
    }

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

    public ActionGroup(ActionData data, Set<ExecutableAction> actions, ActionGroupCallbacks callbacks) {
        this(
            data,
            new IterableRange<>(1, 1, val -> ++val),
            null,
            actions,
            callbacks
        );
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
                        callbacks.getChosenActions(this.actions).stream()
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
                        callbacks.getChosenActions(this.actions).stream()
                                .map(action -> action.run(context))
                                .collect(Collectors.toCollection(LinkedList::new))
                );
            }
        }

        return results.toArray();
    }
}
