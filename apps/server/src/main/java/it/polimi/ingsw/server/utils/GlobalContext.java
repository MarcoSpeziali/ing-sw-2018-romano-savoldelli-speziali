package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.net.mocks.IPlayer;

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
     * @param matchId the id of the match the player is in
     * @return the {@link Context} for the provided {@code player}
     */
    public Context getContextForPlayer(IPlayer player, int matchId) {
        String key = String.format("%d/%s", matchId, player.getUsername());
        
        if (this.containsKey(key)) {
            return (Context) this.get(key);
        }
        else {
            Context playerContext = (Context) this.put(key, this.snapshot(key));
            playerContext.put(Context.CURRENT_PLAYER, player);

            return playerContext;
        }
    }
}
