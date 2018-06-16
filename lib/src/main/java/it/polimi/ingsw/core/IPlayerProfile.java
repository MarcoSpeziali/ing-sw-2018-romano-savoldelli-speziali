package it.polimi.ingsw.core;

import java.io.Serializable;

public interface IPlayerProfile extends Serializable {
    Integer getPlayerId();

    String getUserName();

    Integer getPlayedGamesCount();

    Integer getTotalWins();

    default Integer getTotalLosses() {
        Integer totalGames = getPlayedGamesCount();
        Integer totalWins = getTotalWins();

        return totalGames - totalWins;
    }

    Integer getTotalPoints();
}
