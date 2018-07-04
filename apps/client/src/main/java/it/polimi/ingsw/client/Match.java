package it.polimi.ingsw.client;

import it.polimi.ingsw.controllers.MatchController;
import javafx.scene.layout.StackPane;

public final class Match {

    private static MatchController matchController;
    private static StackPane outerPane;

    public static byte performedAction = 0;

    public static MatchController getMatchController() {
        return matchController;
    }

    public static void setMatchController(MatchController matchController) {
        Match.matchController = matchController;
    }

    public static StackPane getOuterPane() {
        return outerPane;
    }

    public static void setOuterPane(StackPane outerPane) {
        Match.outerPane = outerPane;
    }

}
