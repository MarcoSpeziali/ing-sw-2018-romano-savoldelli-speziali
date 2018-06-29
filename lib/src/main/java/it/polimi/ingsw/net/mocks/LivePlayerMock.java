package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class LivePlayerMock implements ILivePlayer {

    private static final long serialVersionUID = -4545200409361100385L;

    private final int favourTokens;
    private final IWindow window;
    private final IPlayer player;
    private final boolean left;
    
    public LivePlayerMock(IPlayer iPlayer) {
        this(
                0,
                null,
                new PlayerMock(iPlayer),
                false
        );
    }
    
    public LivePlayerMock(ILivePlayer iLivePlayer) {
        this(
                iLivePlayer.getFavourTokens(),
                iLivePlayer.getWindow(),
                new PlayerMock(iLivePlayer.getPlayer()),
                iLivePlayer.hasLeft()
        );
    }

    @JSONDesignatedConstructor
    public LivePlayerMock(
            @JSONElement("favour-tokens") int favourTokens,
            @JSONElement("window") IWindow window,
            @JSONElement("player") IPlayer player,
            @JSONElement("left") boolean left
    ) {
        this.favourTokens = favourTokens;
        this.window = window;
        this.player = player;
        this.left = left;
    }

    @Override
    @JSONElement("favour-tokens")
    public int getFavourTokens() {
        return favourTokens;
    }

    @Override
    @JSONElement("window")
    public IWindow getWindow() {
        return window;
    }

    @Override
    @JSONElement("player")
    public IPlayer getPlayer() {
        return player;
    }
    
    @Override
    @JSONElement("left")
    public boolean hasLeft() {
        return left;
    }
}
