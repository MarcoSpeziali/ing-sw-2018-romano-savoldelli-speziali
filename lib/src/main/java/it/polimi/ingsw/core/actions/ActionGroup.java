package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.Range;

import java.util.List;
import java.util.function.Consumer;

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
    private List<ExecutableAction> actions;

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
    public List<ExecutableAction> getActions() {
        return this.actions;
    }

    /**
     * @return The callbacks used to interact with the player.
     */
    public ActionGroupCallbacks getCallbacks() {
        return this.callbacks;
    }

    /**
     * @param data The data of the action group.
     * @param repetitionNumber A range of repetitions.
     * @param chooseBetween The number of actions to be chosen.
     * @param actions The actions to be executed.
     * @param callbacks The callbacks used to interact with the player.
     */
    public ActionGroup(
            ActionData data,
            IterableRange<Integer> repetitionNumber,
            Range<Integer> chooseBetween,
            List<ExecutableAction> actions,
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

        context.snapshot("ActionGroup{" + this.data.getId() + "}", snapshot -> {
            // Executes the mandatory repetitions
            this.executeMandatoryRepetitions(snapshot);

            // Executes the optional repetitions
            this.executeOptionalRepetitions(snapshot);
        });

        return null;
    }

    /**
     * Executes the mandatory repetitions.
     * @param snapshot The {@link Context.Snapshot} that holds the variables of the current {@link ActionGroup}.
     */
    private void executeMandatoryRepetitions(Context.Snapshot snapshot) {
        // If the range is 3..7 the first 3 iterations are mandatory
        for (int i = 0; i < this.repetitionNumber.getStart(); i++) {
            // If chooseBetween is null every actions needs to be executed
            if (this.chooseBetween == null) {
                //noinspection SimplifyStreamApiCallChains
                this.actions.forEach(getActionConsumer(snapshot));
            }
            // Otherwise only a subset of them get executed
            else {
                //noinspection SimplifyStreamApiCallChains
                callbacks.getChosenActions(this.actions, this.chooseBetween)
                        .forEach(getActionConsumer(snapshot));
            }
        }
    }

    /**
     * Executes the optional repetitions.
     * @param snapshot The {@link Context.Snapshot} that holds the variables of the current {@link ActionGroup}.
     */
    private void executeOptionalRepetitions(Context.Snapshot snapshot) {
        // The mandatory repetitions have already been executed, now the optional ones needs to be processed
        for (int index = this.repetitionNumber.getStart(); index < this.repetitionNumber.getEnd(); index++) {
            // Whenever shouldRepeat returns false the process stops
            if (!this.callbacks.shouldRepeat(
                    index,
                    this.repetitionNumber.getEnd())) {
                break;
            }

            // If chooseBetween is null every actions needs to be executed
            if (this.chooseBetween == null) {
                //noinspection SimplifyStreamApiCallChains
                this.actions.forEach(getActionConsumer(snapshot));
            }
            // Otherwise only a subset of them get executed
            else {
                //noinspection SimplifyStreamApiCallChains
                callbacks.getChosenActions(this.actions, this.chooseBetween)
                        .forEach(getActionConsumer(snapshot));
            }
        }
    }

    private Consumer<? super ExecutableAction> getActionConsumer(Context.Snapshot snapshot) {
        return executableAction -> {
            if (executableAction.getActionData().getResultIdentifier() == null) {
                executableAction.run(snapshot);
            }
            else {
                snapshot.put(executableAction.getActionData().getResultIdentifier(), executableAction.run(snapshot));
            }
        };
    }
}
