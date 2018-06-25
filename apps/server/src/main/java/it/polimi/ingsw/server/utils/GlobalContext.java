package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.server.sql.DatabasePlayer;

/**
 * Represents the context globally shader between players.
 */
public class GlobalContext extends Context {

    private static final long serialVersionUID = -7461873163249453751L;

    /**
     * Holds the single instance of this class.
     */
    private static GlobalContext singleton;

    private GlobalContext() {
    }

    /**
     * @return the shares instance of {@link GlobalContext}
     */
    public static synchronized GlobalContext getGlobalContext() {
        if (singleton == null) {
            singleton = new GlobalContext();
        }

        return singleton;
    }

    /**
     * If the context exists it gets returned, otherwise it gets created.
     *
     * @param player the player owning the context
     * @return the {@link Context} for the provided {@code player}
     */
    public Context getContextForPlayer(DatabasePlayer player) {
        if (this.containsKey(player.getUsername())) {
            return (Context) this.get(player.getUsername());
        }
        else {
            String username = player.getUsername();

            Context playerContext = (Context) this.put(username, this.snapshot(username));
            playerContext.put(Context.CURRENT_PLAYER, player);

            return playerContext;
        }
    }
}
