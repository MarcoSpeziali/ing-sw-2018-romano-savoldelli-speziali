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
        // ho dato un occhio bene finalmente alle classi tue (Marco) e noto che effect fa tutto:
        // insomma si occupa delle action (che ovviamente sono dentro effect nell'xml) e mi pare che valuti anche
        // le constraints, solo che non ne sono sicuro e pensavo di dover fare qualche riferimento a constraints
        // anche qui... la situazione si è complicata perchè hai generalizzato molto e si perde un po' la visione complessiva
        this.effect.run(cardId);
        this.usedOnce = true;
    }
}
