package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.Objective;
import it.polimi.ingsw.utils.text.LocalizedString;

public class ObjectiveCard extends Card {
    private String cardId;
    private CardVisibility visibility;
    private int pointsPerCompletion;
    private Objective objective;

    public ObjectiveCard(String cardId, CardVisibility visibility, int pointsPerCompletion, Image backImage, Image frontImage, LocalizedString title, LocalizedString description ) {
        super(backImage, frontImage, title, description);
        this.cardId = cardId;
        this.visibility = visibility;
        this.pointsPerCompletion = pointsPerCompletion;
    }

    public String getCardId() {
        return cardId;
    }

    public CardVisibility getVisibility() {
        return visibility;
    }

    public int getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    public Objective getObjective() {
        return objective;
    }
}
