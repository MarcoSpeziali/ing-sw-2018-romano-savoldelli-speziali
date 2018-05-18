package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.Objective;
import it.polimi.ingsw.utils.text.LocalizedString;

public class ObjectiveCard extends Card {
    private String cardId;
    private CardVisibility visibility;
    private Objective objective;

    /**
     * Sets up a new objective card
     * @param cardId is the card's ID
     * @param visibility is the card's visibility
     * @param title is the card's title
     * @param description is the card's description
     * @param objective is the card's objective
     */
    public ObjectiveCard(String cardId, CardVisibility visibility, String title, String description, Objective objective) {
        super(title,description);
        this.cardId = cardId;
        this.visibility = visibility;
        this.objective = objective;
    }

    /**
     * @return the card's id
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * @return the card's visibility
     */
    public CardVisibility getVisibility() {
        return this.visibility;
    }

    /**
     * @return the amount of point of the card
     */

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @param visibility is the card's visibility
     */
    public void setVisibility(CardVisibility visibility) {
        this.visibility = visibility;
    }

    /**
     * @param objective is the card's objective
     */
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    /**
     * @return the card's objective
     */
    public Objective getObjective() {
        return this.objective;
    }
}
