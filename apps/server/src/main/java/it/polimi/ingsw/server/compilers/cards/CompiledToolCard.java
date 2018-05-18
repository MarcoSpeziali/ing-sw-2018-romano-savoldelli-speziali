package it.polimi.ingsw.server.compilers.cards;

import it.polimi.ingsw.server.compilers.effects.CompiledEffect;

public class CompiledToolCard extends CompiledCard {

    private static final long serialVersionUID = 7732279924971784573L;

    /**
     * The effect of the tool card.
     */
    private CompiledEffect effect;

    /**
     * @return the effect of the tool card
     */
    public CompiledEffect getEffect() {
        return effect;
    }

    /**
     * @param id   the id of the card
     * @param name the name key of the card
     * @param effect the effect of the tool card
     */
    public CompiledToolCard(String id, String name, CompiledEffect effect) {
        super(id, name);
        this.effect = effect;
    }

    /**
     * @param compiledCard the compiled card
     * @param effect the effect of the tool card
     */
    public CompiledToolCard(CompiledCard compiledCard, CompiledEffect effect) {
        this(compiledCard.getId(), compiledCard.getName(), effect);
    }
}
