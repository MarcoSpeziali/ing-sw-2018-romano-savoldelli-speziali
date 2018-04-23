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

    public Card (Image backImage, Image frontImage, LocalizedString title, LocalizedString descrtiption){
        this.backImage = backImage;
        this.frontImage = frontImage;
        this.title = title;
        this.description = descrtiption;
    }

    public Card (String backImagePath, String frontImagePath, LocalizedString title, LocalizedString description ){
        this.backImagePath = backImagePath;
        this.frontImagePath = frontImagePath;
        this.title = title;
        this. description = description;

    } //c'è qualcosa che non va con i nomi penso ma non sono sicuro

    public Card (String backImageName, String frontImageName, LocalizedString title, LocalizedString description){
        this.backImageName = backImageName;
        this.frontImageName = frontImageName;
        this.title = title;
        this.description = description;
    }

    public void getBackImage() {
        return this.backImage;
    }

    public Image getFrontImage() {
        return this.frontImage;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}
