package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.WindowMock;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class WindowRequest implements MatchInteraction {
    
    private static final long serialVersionUID = 3597975825187163906L;
    private final int matchId;
    private final IWindow[] windows;
    
    @JSONDesignatedConstructor
    public WindowRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("windows") WindowMock[] windows
    ) {
        this.matchId = matchId;
        this.windows = windows;
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return this.matchId;
    }
    
    @JSONElement("windows")
    public IWindow[] getWindows() {
        return this.windows;
    }
}
