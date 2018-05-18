package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.text.LocalizedString;

public abstract class Card {
    protected Image backImage;
    protected Image frontImage;
    protected LocalizedString title;
    protected LocalizedString description;
    protected String backImagePath;
    protected String frontImagePath;


    /**
     * @param backImage the card's back image
     * @param frontImage the card's front image
     * @param title the card's title
     * @param description the card's description
     */
    public Card (Image backImage, Image frontImage, LocalizedString title, LocalizedString description){

        this.backImage = backImage;
        this.frontImage = frontImage;
        this.title = title;
        this.description = description;
    }

    /**
     * @param backImagePath the card's back image path
     * @param frontImagePath the card's front image path
     * @param title the card's title
     * @param description the card's description
     */
    public Card (String backImagePath, String frontImagePath, LocalizedString title, LocalizedString description ){

        this.backImagePath = backImagePath;
        this.frontImagePath = frontImagePath;
        this.title = title;
        this.description = description;

    }

    /**
     * @return the card's back image
     */
    public Image getBackImage() {
        return this.backImage;
    }

    /**
     * @return the card's back image path
     */
    public String getBackImagePath() {

        return backImagePath;
    }

    /**
     * @return the card's front image path
     */
    public String getFrontImagePath() {
        return frontImagePath;
    }

    /**
     * @return the card's front image
     */
    public Image getFrontImage() {
        return this.frontImage;
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
     * @param backImage the card's back image
     */
    public void setBackImage(Image backImage) {
        this.backImage = backImage;
    }

    /**
     * @param frontImage the card's front image
     */
    public void setFrontImage(Image frontImage) {
        this.frontImage = frontImage;
    }

    /**
     * @param title the card's title
     */
    public void setTitle(LocalizedString title) {
        this.title = title;
    }

    /**
     * @param description the card's description
     */
    public void setDescription(LocalizedString description) {
        this.description = description;
    }

    /**
     * @param backImagePath the card's back image path
     */
    public void setBackImagePath(String backImagePath) {
        this.backImagePath = backImagePath;
    }

    /**
     * @param frontImagePath the card's front image path
     */
    public void setFrontImagePath(String frontImagePath) {
        this.frontImagePath = frontImagePath;
    }

}
