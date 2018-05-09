package it.polimi.ingsw.compilers.actions;

import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.ActionGroup;
import it.polimi.ingsw.core.actions.ExecutableAction;
import it.polimi.ingsw.utils.IterableRange;

import java.util.List;

// TODO: docs
public class CompiledActionGroup implements CompiledExecutableAction {
    private ActionData actionData;
    private String rootActionId;
    private List<CompiledExecutableAction> actions;
    private IterableRange<Integer> repetitions;
    private IterableRange<Integer> chooseBetween;

    public ActionData getActionData() {
        return actionData;
    }

    public void setActionData(ActionData actionData) {
        this.actionData = actionData;
    }

    public List<CompiledExecutableAction> getActions() {
        return actions;
    }

    public void setActions(List<CompiledExecutableAction> actions) {
        this.actions = actions;
    }

    public IterableRange<Integer> getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(IterableRange<Integer> repetitions) {
        this.repetitions = repetitions;
    }

    public IterableRange<Integer> getChooseBetween() {
        return chooseBetween;
    }

    public void setChooseBetween(IterableRange<Integer> chooseBetween) {
        this.chooseBetween = chooseBetween;
    }

    public String getRootActionId() {
        return rootActionId;
    }

    public void setRootActionId(String rootActionId) {
        this.rootActionId = rootActionId;
    }

    @Override
    public Class<? extends ExecutableAction> getClassToInstantiate() {
        return ActionGroup.class;
    }
}
