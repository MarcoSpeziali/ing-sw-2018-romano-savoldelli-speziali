package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;

@FunctionalInterface
public interface VariableSupplier<T> {
    /**
     * Gets a result.
     *
     * @param context The context.
     * @return a result
     */
    T get(Context context);
}
