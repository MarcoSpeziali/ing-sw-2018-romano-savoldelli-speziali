package it.polimi.ingsw.core;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.models.*;

import java.io.Serializable;

public class Match {

    public static MatchController getMatchController() {
        return matchController;
    }

    private static MatchController matchController;

    public static void setMatchController(MatchController matchController) {
        Match.matchController = matchController;
    }
}
