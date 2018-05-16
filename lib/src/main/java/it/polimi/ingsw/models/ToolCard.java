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

    public ToolCard(String cardId, String nameKey, int initialCost, Effect effect, Image backImage, Image frontImage, LocalizedString title, LocalizedString description) {
        super(backImage, frontImage, title, description);
        this.cardId = cardId;
        this.nameKey = nameKey;
        this.initialCost = initialCost;
        this.effect = effect;
        this.usedOnce = false;
    }

    public String getCardId() {
        return cardId;
    }

    public String getNameKey() {
        return nameKey;
    }

    public int getInitialCost() {
        return initialCost;
    }

    public boolean isUsedOnce() {
        return usedOnce;
    }

    public Effect getEffect() {
        return effect;
    }

    public void activate() {
        this.effect.run(cardId);
        this.usedOnce = true;
    }
}
