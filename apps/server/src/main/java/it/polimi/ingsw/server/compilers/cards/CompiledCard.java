package it.polimi.ingsw.server.compilers.cards;

import java.io.Serializable;

public class CompiledCard implements Serializable {

    private static final long serialVersionUID = 4710162464640672021L;

    /**
     * The id of the card.
     */
    private String id;

    /**
     * The name key of the card.
     */
    private String name;

    /**
     * @return the id of the card
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name key of the card
     */
    public String getName() {
        return name;
    }

    /**
     * @param id the id of the card
     * @param name the name key of the card
     */
    public CompiledCard(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
