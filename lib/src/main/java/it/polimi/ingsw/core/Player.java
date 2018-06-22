package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class Player implements JSONSerializable, ILivePlayer, RandomPutLocation, RandomPickLocation {

    private static final long serialVersionUID = 513182840776549527L;

    private static Player sharedInstance;

    public static Player getCurrentPlayer() {
        return sharedInstance;
    }

    private final int id;
    private final String username;
    private final int tokenCount;
    private final Window window;

    private Die heldDie;

    @SuppressWarnings("squid:S3010")
    @JSONDesignatedConstructor
    Player(
            @JSONElement("id") int id,
            @JSONElement("username") String username,
            @JSONElement("favour-tokens") int tokenCount,
            @JSONElement("window") Window window,
            @JSONElement("held-die") Die heldDie
    ) {
        this.id = id;
        this.username = username;
        this.tokenCount = tokenCount;
        this.window = window;
        this.heldDie = heldDie;

        sharedInstance = this;
    }

    @Override
    @JSONElement("id")
    public int getId() {
        return this.id;
    }

    @Override
    @JSONElement("username")
    public String getUsername() {
        return this.username;
    }

    @JSONElement("favour-tokens")
    public int getFavourTokens() {
        return tokenCount;
    }

    @JSONElement("window")
    public Window getWindow() {
        return window;
    }

    @Override
    public int getNumberOfDice() {
        return this.heldDie == null ? 0 : 1;
    }

    @Override
    public int getFreeSpace() {
        return 1 - this.getNumberOfDice();
    }

    @Override
    public Die pickDie() {
        Die die = this.heldDie;
        this.heldDie = null;
        return die;
    }

    @Override
    public void putDie(Die die) {
        if (this.heldDie != null) {
            throw new FullLocationException(this);
        }

        this.heldDie = die;
    }

    @JSONElement("held-die")
    public Die getHeldDie() {
        return heldDie;
    }
}
