package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.text.LocalizedString;

import java.io.Serializable;

public abstract class Card implements Serializable {

    private static final long serialVersionUID = -3678332528456922553L;

    protected LocalizedString title;
    protected LocalizedString description;

    /**
     * @param title       the card's title
     * @param description the card's description
     */
    public Card(String title, String description) {
        this.title = new LocalizedString(title);
        this.description = new LocalizedString(description);
    }

    /**
     * @return the card's title
     */
    public LocalizedString getTitle() {
        return this.title;
    }

    /**
     * @param title the card's title
     */
    public void setTitle(String title) {
        this.title = new LocalizedString(title);
    }

    /**
     * @return the card's description
     */
    public LocalizedString getDescription() {
        return this.description;
    }

    /**
     * @param description the card's description
     */
    public void setDescription(String description) {
        this.description = new LocalizedString(description);
    }
}