package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.ObjectiveCard;

public class Player implements RandomPickLocation, RandomPutLocation {
    private static final long serialVersionUID = -2840357342425816145L;

    private IPlayerProfile profile;
    private ObjectiveCard[] privateObjectiveCard;
    private byte favourTokenCount;
    private Die pickedDie;

    private static Player currentPlayer;

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets up a new {@link Player}
     * @param profile is the player's profile
     * @param privateObjectiveCard is the player's private objective card
     * @param favourTokenCount the count of initial tokens
     */
    public Player(IPlayerProfile profile, ObjectiveCard[] privateObjectiveCard, byte favourTokenCount){
        this.profile = profile;
        this.privateObjectiveCard = privateObjectiveCard;
        this.favourTokenCount = favourTokenCount;

        currentPlayer = this;
    }

    /**
     * @return the player's profile
     */
    public IPlayerProfile getProfile() {
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
     * @param profile is the player's name
     */
    public void setProfile(IPlayerProfile profile) {
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

    @Override
    public Die pickDie() {
        Die die = this.pickedDie;
        this.pickedDie = null;

        return die;
    }

    @Override
    public int getNumberOfDice() {
        return 1 - getFreeSpace();
    }

    @Override
    public void putDie(Die die) {
        this.pickedDie = die;
    }

    @Override
    public int getFreeSpace() {
        return this.pickedDie == null ? 1 : 0;
    }
}
