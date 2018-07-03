package it.polimi.ingsw.client;

import it.polimi.ingsw.controllers.MatchController;
import javafx.scene.layout.StackPane;

public final class Match {

    private static MatchController matchController;
    private static StackPane outerPane;
    private static byte performedAction;

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

    public static byte getPerformedAction() {
        return performedAction;
    }

    public static void setPerformedAction(byte performedAction) {
        Match.performedAction = performedAction;
    }
}
