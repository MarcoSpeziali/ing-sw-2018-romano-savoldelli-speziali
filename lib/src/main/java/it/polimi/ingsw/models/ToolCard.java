package it.polimi.ingsw.models;

import it.polimi.ingsw.core.IEffect;

public class ToolCard extends Card {

    private String cardId;
    private IEffect effect;

    /**
     * @return the tool card's id
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * @return the tool card's effect
     */
    public IEffect getEffect() {
        return this.effect;
    }

    /**
     * @param cardId is the card's id
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @param effect is the effect of the tool card
     */
    public void setEffect(IEffect effect) {
        this.effect = effect;
    }

    /**
     * Sets up a new {@link ToolCard}
     * @param cardId is the card's Id
     * @param nameKey is the card's name
     * @param effect is the card's effect
     * @param description is the card's description
     */
    public ToolCard(String cardId, String nameKey, String description, IEffect effect) {
        super(nameKey, description);
        this.cardId = cardId;
        this.effect = effect;
    }

    /**
     * Activates the effect of the tool card.
     */
    public void activate() {
        this.effect.run(cardId);
    }
}
