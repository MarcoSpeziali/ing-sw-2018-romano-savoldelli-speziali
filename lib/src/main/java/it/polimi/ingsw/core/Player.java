package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.listeners.OnToolCardUsedListener;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.models.ToolCard;

import java.util.LinkedList;
import java.util.List;

public class Player implements RandomPickLocation, RandomPutLocation {
    private static final long serialVersionUID = -2840357342425816145L;

    private IPlayerProfile profile;
    private ObjectiveCard[] privateObjectiveCard;
    private byte favourTokenCount;
    private Die pickedDie;

    private static Player currentPlayer;

    List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();
    List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    List<OnToolCardUsedListener> onToolCardUsedListeners = new LinkedList<>();

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

    public void useToolCard(ToolCard toolCard) {
        toolCard.activate();
        this.onToolCardUsedListeners.forEach(listener -> listener.onToolCardUsed(toolCard));
    }


    @Override
    public Die pickDie() {
        Die die = this.pickedDie;
        this.pickedDie = null;
        this.onDiePickedListeners.forEach(listener -> listener.onDiePicked(die));

        return die;
    }

    @Override
    public int getNumberOfDice() {
        return 1 - getFreeSpace();
    }

    @Override
    public void putDie(Die die) {
        this.pickedDie = die;
        this.onDiePutListeners.forEach(listener -> listener.onDiePut(die));
    }

    @Override
    public int getFreeSpace() {
        return this.pickedDie == null ? 1 : 0;
    }


    public void addListener(OnDiePickedListener onDiePickedListener) {
        this.onDiePickedListeners.add(onDiePickedListener);
    }

    public void addListener(OnDiePutListener onDiePutListener) {
        this.onDiePutListeners.add(onDiePutListener);
    }

    public void addListener(OnToolCardUsedListener onToolCardUsedListener) {
        this.onToolCardUsedListeners.add(onToolCardUsedListener);
    }
}
