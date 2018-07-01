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
     * @param matchId the id of the match the player is in
     * @return the {@link Context} for the provided {@link it.polimi.ingsw.net.mocks.IMatch}
     */
    public Context getContextForMatch(int matchId) {
        String key = String.valueOf(matchId);
        
        if (this.containsKey(key)) {
            return (Context) this.get(key);
        }
        else {
            return (Context) this.put(key, this.snapshot(key));
        }
    }

    public Context getContextForPlayer(IPlayer player, int matchId) {
        String key = player.getUsername();

        Context matchContext = getContextForMatch(matchId);

        if (matchContext.containsKey(key)) {
            return (Context) matchContext.get(key);
        }
        else {
            return (Context) matchContext.put(key, this.snapshot(key));
        }
    }
}
