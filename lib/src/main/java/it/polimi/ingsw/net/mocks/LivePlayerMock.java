package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class LivePlayerMock implements ILivePlayer {

    private static final long serialVersionUID = -4545200409361100385L;

    private final int favourTokens;
    private final IWindow window;
    private final IPlayer player;

    public LivePlayerMock(ILivePlayer iLivePlayer) {
        this(
                iLivePlayer.getFavourTokens(),
                iLivePlayer.getWindow(),
                iLivePlayer.getPlayer()
        );
    }

    @JSONDesignatedConstructor
    public LivePlayerMock(
            @JSONElement("favour-tokens") int favourTokens,
            @JSONElement("window") IWindow window,
            @JSONElement("player") IPlayer player
    ) {
        this.favourTokens = favourTokens;
        this.window = window;
        this.player = player;
    }

    @Override
    public int getFavourTokens() {
        return favourTokens;
    }

    @Override
    public IWindow getWindow() {
        return window;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }
}
