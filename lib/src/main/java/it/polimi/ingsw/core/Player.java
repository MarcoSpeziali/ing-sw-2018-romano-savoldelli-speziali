package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDieUsedListener;
import it.polimi.ingsw.listeners.OnToolCardUsedListener;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.models.ToolCard;

import java.util.LinkedList;
import java.util.List;

public class Player implements RandomPickLocation, RandomPutLocation {
    private static final long serialVersionUID = -2840357342425816145L;
    private static Player currentPlayer;
    List<OnDieUsedListener> onDieUsedListeners = new LinkedList<>();
    List<OnToolCardUsedListener> onToolCardUsedListeners = new LinkedList<>();
    private IPlayerProfile profile;
    private ObjectiveCard[] privateObjectiveCard;
    private byte favourTokenCount;
    private Die pickedDie;

    /**
     * Sets up a new {@link Player}
     *
     * @param profile              is the player's profile
     * @param privateObjectiveCard is the player's private objective card
     * @param favourTokenCount     the count of initial tokens
     */
    public Player(IPlayerProfile profile, ObjectiveCard[] privateObjectiveCard, byte favourTokenCount) {
        this.profile = profile;
        this.privateObjectiveCard = privateObjectiveCard;
        this.favourTokenCount = favourTokenCount;

        currentPlayer = this;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the player's profile
     */
    public IPlayerProfile getProfile() {
        return profile;
    }

    /**
     * @param profile is the player's name
     */
    public void setProfile(IPlayerProfile profile) {
        this.profile = profile;
    }

    /**
     * @return the player's private objective card
     */
    public ObjectiveCard[] getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    /**
     * @param privateObjectiveCard is the player's private objective card
     */
    public void setPrivateObjectiveCard(ObjectiveCard[] privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    /**
     * @return the player's amount of favour token
     */
    public byte getFavourTokenCount() {
        return favourTokenCount;
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
        this.onDieUsedListeners.forEach(OnDieUsedListener::onDieUsed);

        return die;
    }

    @Override
    public int getNumberOfDice() {
        return 1 - getFreeSpace();
    }

    @Override
    public void putDie(Die die) {
        this.pickedDie = die;
        this.onDieUsedListeners.forEach(OnDieUsedListener::onDieUsed);
    }

    @Override
    public int getFreeSpace() {
        return this.pickedDie == null ? 1 : 0;
    }


    public void addListener(OnDieUsedListener onDieUsedListener) {
        this.onDieUsedListeners.add(onDieUsedListener);
    }

    public void addListener(OnToolCardUsedListener onToolCardUsedListener) {
        this.onToolCardUsedListeners.add(onToolCardUsedListener);
    }
}
