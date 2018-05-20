package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;

import java.io.Serializable;

/**
 * Represents an executable action which could be a single or a grouped action.
 */
public interface ExecutableAction extends Serializable {

    /**
     * @return The data of the action.
     */
    ActionData getActionData();

    /**
     * Runs the action.
     * @param context The context that holds the variables needed by the action.
     * @return The result of the action (if any).
     */
    Object run(Context context);
}
