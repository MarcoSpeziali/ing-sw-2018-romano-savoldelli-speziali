package it.polimi.ingsw.models;

import it.polimi.ingsw.net.mocks.ICard;

public abstract class Card implements ICard {

    private static final long serialVersionUID = -3678332528456922553L;

    protected String title;
    protected String description;

    /**
     * @param title       the card's title
     * @param description the card's description
     */
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * @return the card's title
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the card's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the card's description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the card's description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}