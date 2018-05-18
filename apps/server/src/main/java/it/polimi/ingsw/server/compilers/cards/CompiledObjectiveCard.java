package it.polimi.ingsw.server.compilers.cards;

import it.polimi.ingsw.server.compilers.objectives.CompiledObjective;
import it.polimi.ingsw.core.CardVisibility;

public class CompiledObjectiveCard extends CompiledCard {

    private static final long serialVersionUID = 5429388051529190502L;

    /**
     * The objective of the card.
     */
    private CompiledObjective objective;

    /**
     * The visibility of the card.
     */
    private CardVisibility visibility;

    /**
     * @return the objective of the card
     */
    public CompiledObjective getObjective() {
        return objective;
    }

    /**
     * @return the visibility of the card
     */
    public CardVisibility getVisibility() {
        return visibility;
    }

    /**
     * @param id   the id of the card
     * @param name the name key of the card
     * @param objective the objective of the card
     */
    public CompiledObjectiveCard(String id, String name, CompiledObjective objective, CardVisibility visibility) {
        super(id, name);
        this.objective = objective;
        this.visibility = visibility;
    }

    /**
     * @param compiledCard the compiled card
     * @param objective the objective of the card
     */
    public CompiledObjectiveCard(CompiledCard compiledCard, CompiledObjective objective, CardVisibility visibility) {
        this(compiledCard.getId(), compiledCard.getName(), objective, visibility);
    }
}
