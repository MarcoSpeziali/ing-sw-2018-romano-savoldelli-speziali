package it.polimi.ingsw.models;

public class Player {
    private String profile; //FIXME dopo update dell'app client
    private ObjectiveCard[] privateObjectiveCard; //controllare lunghezza array
    private byte favourTokenCount;
    private Die pickedDie;

    public Player(String profile, ObjectiveCard[] privateObjectiveCard, byte favourTokenCount, Die pikedDie ){
        this.profile = profile;
        this.privateObjectiveCard = privateObjectiveCard;
        this.favourTokenCount = favourTokenCount;
        this.pickedDie = pikedDie;
    }

    public String getProfile() {
        return profile;
    }

    public ObjectiveCard[] getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public byte getFavourTokenCount() {
        return favourTokenCount;
    }

    public Die getPickedDie() {
        return pickedDie;
    }
}
