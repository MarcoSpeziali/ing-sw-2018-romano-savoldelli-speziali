package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.Objective;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.GlobalContext;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultsManager {
    
    private ResultsManager() {}
    
    @SuppressWarnings("squid:S1602")
    public static Map<DatabasePlayer, Integer> getPartialPointsForPlayers(int matchId, DatabasePlayer[] players, Objective[] publicObjectives) {
        return Arrays.stream(players)
                .map(databasePlayer -> {
                    Context playerContext = GlobalContext.getGlobalContext()
                    .getContextForPlayer(databasePlayer, matchId);
            
                    return Map.entry(
                            databasePlayer,
                            Arrays.stream(publicObjectives)
                                    .mapToInt(objective -> objective.calculatePoints(playerContext))
                                    .sum()
                    );
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public static Map<DatabasePlayer, Integer> integratePartialResultsWithPrivateObjectives(int matchId, Map<DatabasePlayer, Integer> partialResults, Map<DatabasePlayer, Objective> privateCards) {
        return partialResults.entrySet().stream()
                .peek(entry -> {
                    Context playerContext = GlobalContext.getGlobalContext()
                            .getContextForPlayer(entry.getKey(), matchId);
                    
                    entry.setValue(
                            entry.getValue() + privateCards.get(entry.getKey()).calculatePoints(playerContext)
                    );
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public static Map<DatabasePlayer, Integer> finalizeWithPenaltiesAndBonus(int matchId, Map<DatabasePlayer, Integer> partialResults, MatchObjectsManager matchObjectsManager) {
        return partialResults.entrySet().stream()
                .peek(entry -> {
                    Context playerContext = GlobalContext.getGlobalContext()
                            .getContextForPlayer(entry.getKey(), matchId);
                    
                    int remainingFavourTokens = matchObjectsManager.getFavourTokensMap().get(entry.getKey());
                    
                    int blankCellsInWindow = ((Window) playerContext.get(Context.WINDOW)).getFreeSpace();
    
                    entry.setValue(
                            entry.getValue() + remainingFavourTokens - blankCellsInWindow
                    );
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
