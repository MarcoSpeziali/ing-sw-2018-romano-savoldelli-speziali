package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.PlayerMock;
import it.polimi.ingsw.server.managers.MatchObjectsManager;
import it.polimi.ingsw.server.managers.turns.PlayerTurnList;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;

public class MatchContextHelper {

    private MatchContextHelper() {}

    public static void init(Context matchContext, DatabaseMatch databaseMatch, MatchObjectsManager matchObjectsManager, PlayerTurnList playerTurnList) {
        matchContext.put(Context.DRAFT_POOL, matchObjectsManager.getDraftPoolController().getDraftPool());
        matchContext.put(Context.ROUND_TRACK, matchObjectsManager.getRoundTrackController().getRoundTrack());
        matchContext.put(Context.BAG, matchObjectsManager.getBag());
        matchContext.put(Context.TURN_LIST, playerTurnList);

        for (DatabasePlayer player : databaseMatch.getDatabasePlayers()) {
            Context playerContext = getContextForPlayer(matchContext, player);
            playerContext.put(Context.WINDOW, matchObjectsManager.getWindowControllerForPlayer(player).getWindow());
            playerContext.put(Context.CURRENT_PLAYER, new PlayerMock(player));
        }
    }

    public static Context getContextForPlayer(Context matchContext, IPlayer player) {
        String key = player.getUsername();

        if (matchContext.containsKey(key)) {
            return (Context) matchContext.get(key);
        }
        else {
            Context context = matchContext.snapshot(key);
            matchContext.put(key, context);

            return context;
        }
    }
}
