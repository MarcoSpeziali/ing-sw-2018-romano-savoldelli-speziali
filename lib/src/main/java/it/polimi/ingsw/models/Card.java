package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import it.polimi.ingsw.utils.text.LocalizedString;

public abstract class Card implements JSONSerializable {

    private static final long serialVersionUID = -3678332528456922553L;

    @JSONElement("title")
    protected String title;
    
    @JSONElement("description")
    protected String description;

    /**
     * @param title       the card's title
     * @param description the card's description
     */
    @JSONDesignatedConstructor
    public Card(
            @JSONElement("title") String title,
            @JSONElement("description") String description
    ) {
        this.title = title;
        this.description = description;
    }

    /**
     * @return the card's title
     */
    public String getTitle() {
        return new LocalizedString(this.title).toString();
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
    public String getDescription() {
        return new LocalizedString(this.description).toString();
    }

    /**
     * @param description the card's description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}