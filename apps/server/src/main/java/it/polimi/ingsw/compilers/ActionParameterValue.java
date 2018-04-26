package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.VariableSupplier;

public class ActionParameterValue {
    boolean needsToBeComputed;
    String value;

    public ActionParameterValue(String value, boolean needsToBeComputed) {
        this.value = value;
        this.needsToBeComputed = needsToBeComputed;
    }

    public Object getValue() {
        if (needsToBeComputed) {
            return (VariableSupplier)(Context context) -> context.get(value);
        }

        return value;
    }
}
