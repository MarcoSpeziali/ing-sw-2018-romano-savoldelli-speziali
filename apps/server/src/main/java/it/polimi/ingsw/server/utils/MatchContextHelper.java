package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.server.managers.MatchObjectsManager;
import it.polimi.ingsw.server.managers.turns.PlayerTurnList;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.util.Map;

public class MatchContextHelper {

    private MatchContextHelper() {}

    public static void init(Context matchContext, DatabaseMatch databaseMatch, MatchObjectsManager matchObjectsManager, PlayerTurnList playerTurnList) {
        matchContext.put(Context.DRAFT_POOL, matchObjectsManager.getDraftPoolController().getDraftPool());
        matchContext.put(Context.ROUND_TRACK, matchObjectsManager.getRoundTrackController().getRoundTrack());
        matchContext.put(Context.BAG, matchObjectsManager.getBag());
        matchContext.put(Context.TURN_LIST, playerTurnList);

        Map<DatabasePlayer, Player> databasePlayerToLivePlayerMap = matchObjectsManager.getDatabasePlayerToLivePlayer();

        for (DatabasePlayer player : databaseMatch.getDatabasePlayers()) {
            Context playerContext = getContextForPlayer(matchContext, player);
            playerContext.put(Context.WINDOW, matchObjectsManager.getWindowControllerForPlayer(player).getWindow());
            playerContext.put(Context.CURRENT_PLAYER, databasePlayerToLivePlayerMap.get(player));
        }
    }

    public static Context getContextForPlayer(Context matchContext, IPlayer player) {
        String key = player.getUsername();

        if (matchContext.containsKey(key)) {
            return (Context) matchContext.get(key);
        }
        else {
            return (Context) matchContext.put(key, matchContext.snapshot(key));
        }
    }
}
