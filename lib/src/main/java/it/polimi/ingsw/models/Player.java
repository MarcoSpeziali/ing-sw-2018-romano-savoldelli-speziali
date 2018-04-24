package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;

public class Player {
    private String profile; //FIXME dopo update dell'app client
    private ObjectiveCard[] privateObjectiveCard; //controllare lunghezza array
    private byte favourTokenCount;
    private Die pickedDie;

    public Player(String profile,ObjectiveCard[] privateObjectiveCard, byte favourTokenCount, Die pikedDie ){
        this.profile = profile;
        this.privateObjectiveCard = privateObjectiveCard;
        this.favourTokenCount = favourTokenCount;
        this.pickedDie = pikedDie;
    }
}
