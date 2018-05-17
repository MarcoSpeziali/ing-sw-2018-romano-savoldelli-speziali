package it.polimi.ingsw.models;
import it.polimi.ingsw.core.Effect;
import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.utils.text.LocalizedString;

public class ToolCard extends Card {

    private String cardId;
    private String nameKey;
    private int initialCost;
    private boolean usedOnce;
    private Effect effect;

    /**
     * Sets up a new {@link ToolCard}
     * @param cardId is the card's Id
     * @param nameKey is the card's name
     * @param initialCost is the card's initial cost
     * @param effect is the card's effect
     * @param backImage is the card's back image
     * @param frontImage is the card's front image
     * @param title is the card's title
     * @param description is the card's description
     */
    public ToolCard(String cardId, String nameKey, int initialCost, Effect effect, Image backImage, Image frontImage, LocalizedString title, LocalizedString description) {
        super(backImage, frontImage, title, description);
        this.cardId = cardId;
        this.nameKey = nameKey;
        this.initialCost = initialCost;
        this.effect = effect;
        this.usedOnce = false;
    }

    /**
     * @return the tool card's id
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * @return the tool card's name
     */
    public String getNameKey() {
        return this.nameKey;
    }

    /**
     * @return the tool card's initial cost
     */
    public int getInitialCost() {
        return this.initialCost;
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
    public Effect getEffect() {
        return this.effect;
    }

    /**
     * Activates the effect of the tool card and sets {@param usedOnce} to true
     */
    public void activate() {
        this.effect.run(cardId);
        this.usedOnce = true;
    }

    /**
     * @param cardId is the card's id
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @param nameKey is the card's name
     */
    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    /**
     * @param initialCost is the card's initial cost
     */
    public void setInitialCost(int initialCost) {
        this.initialCost = initialCost;
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
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
