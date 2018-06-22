package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.io.json.JSONElement;

public interface ILivePlayer extends IPlayer {
    @JSONElement("favour-tokens")
    int getFavourTokens();

    @JSONElement("window")
    Window getWindow();
}
