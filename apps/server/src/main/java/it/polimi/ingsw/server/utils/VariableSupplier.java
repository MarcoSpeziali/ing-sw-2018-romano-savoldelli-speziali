package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;

import java.io.Serializable;

@FunctionalInterface
public interface VariableSupplier<T> extends Serializable {
    /**
     * Gets a result.
     *
     * @param context The context.
     * @return a result
     */
    T get(Context context);
}
