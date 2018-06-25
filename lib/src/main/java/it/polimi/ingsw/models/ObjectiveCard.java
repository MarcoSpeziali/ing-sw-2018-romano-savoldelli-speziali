package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.net.mocks.IObjective;

public class ObjectiveCard extends Card {
    private static final long serialVersionUID = -348932210289943581L;
    
    private String cardId;
    private CardVisibility visibility;
    private IObjective objective;

    /**
     * Sets up a new objective card
     *
     * @param cardId      is the card's id
     * @param visibility  is the card's visibility
     * @param title       is the card's title
     * @param description is the card's description
     * @param objective   is the card's objective
     */
    public ObjectiveCard(String cardId, CardVisibility visibility, String title, String description, IObjective objective) {
        super(title, description);
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
     * @param cardId the amount of point of the card
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @return the card's visibility
     */
    public CardVisibility getVisibility() {
        return this.visibility;
    }

    /**
     * @param visibility is the card's visibility
     */
    public void setVisibility(CardVisibility visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the card's objective
     */
    public IObjective getObjective() {
        return this.objective;
    }

    /**
     * @param objective is the card's objective
     */
    public void setObjective(IObjective objective) {
        this.objective = objective;
    }
}
