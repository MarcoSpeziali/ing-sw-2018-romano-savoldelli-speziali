package it.polimi.ingsw.models;
import

public class ObjectiveCard extends Card{
    private String cardId;
    private CardVisibility visibility;
    private int pointsPerCompletion;
    private Objective objective;

    public ObjectiveCard(String cardId, CardVisibility visibility, int pointsPerCompletion) {
        this.cardId = cardId;
        this.visibility = visibility;
        this.pointsPerCompletion = pointsPerCompletion;
    }
}
