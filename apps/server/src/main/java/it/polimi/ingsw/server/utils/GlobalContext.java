package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Player;

public class GlobalContext extends Context {

    private static final long serialVersionUID = -7461873163249453751L;

    private static GlobalContext singleton;

    public static synchronized GlobalContext getGlobalContext() {
        if (singleton == null) {
            singleton = new GlobalContext();
        }

        return singleton;
    }

    private GlobalContext() { }

    public Context getContextForPlayer(Player player) {
        if (this.containsKey(player.getProfile())) {
            return (Context) this.get(player.getProfile());
        }
        else {
            Context playerContext = (Context) this.put(player.getProfile(), this.snapshot(player.getProfile()));
            playerContext.put(Context.CURRENT_PLAYER, player);
            // TODO: fill values
            return playerContext;
        }
    }
}
