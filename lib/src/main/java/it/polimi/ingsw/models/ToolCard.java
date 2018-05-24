package it.polimi.ingsw.models;

import it.polimi.ingsw.core.IEffect;

public class ToolCard extends Card {

    private String cardId;
    private boolean usedOnce;
    private IEffect effect;

    /**
     * @return the tool card's id
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * @return true if the tool card has already been used, false otherwise
     */
    public boolean isUsedOnce() {
        return this.usedOnce;
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
     * @param usedOnce is true if the tool card is has already been used false otherwise
     */
    public void setUsedOnce(boolean usedOnce) {
        this.usedOnce = usedOnce;
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
        this.usedOnce = false;
    }

    /**
     * Activates the effect of the tool card and sets {@code usedOnce} to {@code true}.
     */
    public void activate() {
        this.usedOnce = true;
        this.effect.run(cardId);
    }
}
