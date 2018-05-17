package it.polimi.ingsw.models;

public class Player {
    private String profile; //FIXME dopo update dell'app client
    private ObjectiveCard[] privateObjectiveCard; //controllare lunghezza array
    private byte favourTokenCount;
    private Die pickedDie;

    /**
     * Sets up a new {@link Player}
     * @param profile is the player's name
     * @param privateObjectiveCard is the player's private objective card
     * @param favourTokenCount
     * @param pikedDie
     */
    public Player(String profile, ObjectiveCard[] privateObjectiveCard, byte favourTokenCount, Die pikedDie ){
        this.profile = profile;
        this.privateObjectiveCard = privateObjectiveCard;
        this.favourTokenCount = favourTokenCount;
        this.pickedDie = pikedDie;
    }

    /**
     * @return the player's name
     */
    public String getProfile() {
        return profile;
    }

    /**
     * @return the player's private objective card
     */
    public ObjectiveCard[] getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    /**
     * @return the player's amount of favour token
     */
    public byte getFavourTokenCount() {
        return favourTokenCount;
    }

    /**
     * @return the die picked by the player
     */
    public Die getPickedDie() {
        return pickedDie;
    }

    /**
     * @param profile is the player's name
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * @param privateObjectiveCard is the player's private objective card
     */
    public void setPrivateObjectiveCard(ObjectiveCard[] privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    /**
     * @param favourTokenCount is the players's amount of favour token
     */
    public void setFavourTokenCount(byte favourTokenCount) {
        this.favourTokenCount = favourTokenCount;
    }

    /**
     * @param pickedDie is the player's picked die
     */
    public void setPickedDie(Die pickedDie) {
        this.pickedDie = pickedDie;
    }
}
