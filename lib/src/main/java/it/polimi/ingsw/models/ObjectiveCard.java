package it.polimi.ingsw.models;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.Objective;
import it.polimi.ingsw.utils.text.LocalizedString;

public class ObjectiveCard extends Card {
    private String cardId;
    private CardVisibility visibility;
    private int pointsPerCompletion;
    private Objective objective;

    /**
     * Sets up a new objective card
     * @param cardId is the card's ID
     * @param visibility is the card's visibility
     * @param pointsPerCompletion is the amount of point for the specific card if the objective is completed
     * @param backImage is the card's back image
     * @param frontImage is the card's front image path
     * @param title is the card's title
     * @param description is the card's description
     * @param objective is the card's objective
     */
    public ObjectiveCard(String cardId, CardVisibility visibility, int pointsPerCompletion, Image backImage,
                         Image frontImage, LocalizedString title, LocalizedString description, Objective objective) {
        super(backImage, frontImage, title, description);
        this.cardId = cardId;
        this.visibility = visibility;
        this.pointsPerCompletion = pointsPerCompletion;
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
    public int getPointsPerCompletion() {
        return this.pointsPerCompletion;
    }

    /**
     * @param cardId is the card's Id
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
     * @param pointsPerCompletion is the card's amount of point if the objective is completed
     */
    public void setPointsPerCompletion(int pointsPerCompletion) {
        this.pointsPerCompletion = pointsPerCompletion;
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
