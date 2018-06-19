package it.polimi.ingsw.models;

import it.polimi.ingsw.net.mocks.IEffect;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ToolCard extends Card {

    private static final long serialVersionUID = 4573356302420625430L;
    
    @JSONElement("id")
    private String cardId;
    
    @JSONElement("effect")
    private IEffect effect;

    /**
     * Sets up a new {@link ToolCard}
     *
     * @param cardId      is the card's Id
     * @param nameKey     is the card's name
     * @param effect      is the card's effect
     * @param description is the card's description
     */
    @JSONDesignatedConstructor
    public ToolCard(
            @JSONElement("id") String cardId,
            @JSONElement("title") String nameKey,
            @JSONElement("description") String description,
            @JSONElement("effect") IEffect effect
    ) {
        super(nameKey, description);
        this.cardId = cardId;
        this.effect = effect;
    }

    /**
     * @return the tool card's id
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * @param cardId is the card's id
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @return the tool card's effect
     */
    public IEffect getEffect() {
        return this.effect;
    }

    /**
     * @param effect is the effect of the tool card
     */
    public void setEffect(IEffect effect) {
        this.effect = effect;
    }

    /**
     * Activates the effect of the tool card.
     */
    public void activate() {
        this.effect.run(cardId);
    }
}
