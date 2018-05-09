package it.polimi.ingsw.compilers.actions.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.VariableSupplier;

// TODO: docs
public class ActionParameterValue {
    private boolean needsToBeComputed;
    private String value;

    public boolean needsToBeComputed() {
        return this.needsToBeComputed;
    }

    public String getRawValue() {
        return this.value;
    }

    public ActionParameterValue(String value, boolean needsToBeComputed) {
        this.value = value;
        this.needsToBeComputed = needsToBeComputed;
    }

    public VariableSupplier getValue() {
        if (needsToBeComputed) {
            return (Context context) -> context.get(value);
        }

        return (Context context) -> value;
    }
}
