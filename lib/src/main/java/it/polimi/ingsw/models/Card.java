package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.scene.image.Image;

public abstract class Card {
    protected LocalizedString title;
    protected LocalizedString description;


    /**
     * @param title the card's title
     * @param description the card's description
     */
    public Card (String title, String description){
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
     * @return the card's description
     */
    public LocalizedString getDescription() {
        return this.description;
    }

    /**
     * @param title the card's title
     */
    public void setTitle(String title) {
        this.title = new LocalizedString(title);
    }

    /**
     * @param description the card's description
     */
    public void setDescription(String description) {
        this.description = new LocalizedString(description);
    }
    }
