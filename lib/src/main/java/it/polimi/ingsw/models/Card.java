package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.text.LocalizedString;

public abstract class Card {
    protected Image backImage;
    protected Image frontImage;
    protected LocalizedString title;
    protected LocalizedString description;
    protected String backImagePath;
    protected String frontImagePath;
    protected String backImageName;
    protected String frontImageName;

    public Card (Image backImage, Image frontImage, LocalizedString title, LocalizedString description){

        this.backImage = backImage;
        this.frontImage = frontImage;
        this.title = title;
        this.description = description;
    }

    public Card (String backImagePath, String frontImagePath, LocalizedString title, LocalizedString description ){

        this.backImagePath = backImagePath;
        this.frontImagePath = frontImagePath;
        this.title = title;
        this.description = description;

    }


    public Image getBackImage() {
        return this.backImage;
    }

    public Image getFrontImage() {
        return this.frontImage;
    }

    public LocalizedString getTitle() {
        return this.title;
    }

    public LocalizedString getDescription() {
        return this.description;
    }
}
