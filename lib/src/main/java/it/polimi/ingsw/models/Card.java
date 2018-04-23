package it.polimi.ingsw.models;

import it.polimi.ingsw.utils.text.LocalizedString;

public abstract class Card {
    protected Image backImage;
    protected Image frontImage;
    protected LocalizedString title;
    protected LocalizedString description;

    public Card (Image backImage, Image frontImage, String title, String descrtiption){

    }

    public Card (String backImagePath, String frontImagePath, String title, String descriptio ){

    }

    public Card (String backImageName, String frontImageName, String titleKey, String descriptionKey){

    }



}
