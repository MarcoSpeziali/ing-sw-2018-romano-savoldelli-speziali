package it.polimi.ingsw.core;

import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class Player implements ILivePlayer {

    private static final long serialVersionUID = 513182840776549527L;

    private static Player sharedInstance;

    public static Player getCurrentPlayer() {
        return sharedInstance;
    }

    private final int id;
    private final String username;
    private final int tokenCount;
    private final IWindow window;

    private DieMock heldDie;

    @SuppressWarnings("squid:S3010")
    @JSONDesignatedConstructor
    public Player(
            @JSONElement("id") int id,
            @JSONElement("username") String username,
            @JSONElement("favour-tokens") int tokenCount,
            @JSONElement("window") WindowMock window,
            @JSONElement("held-die") DieMock heldDie
    ) {
        this.id = id;
        this.username = username;
        this.tokenCount = tokenCount;
        this.window = window;
        this.heldDie = heldDie;

        sharedInstance = this;
    }

    @JSONElement("id")
    public int getId() {
        return this.id;
    }

    @JSONElement("username")
    public String getUsername() {
        return this.username;
    }

    @JSONElement("favour-tokens")
    public int getFavourTokens() {
        return tokenCount;
    }

    @JSONElement("window")
    public IWindow getWindow() {
        return window;
    }

    @Override
    public IPlayer getPlayer() {
        return new PlayerMock(
                this.id,
                this.username
        );
    }
}
