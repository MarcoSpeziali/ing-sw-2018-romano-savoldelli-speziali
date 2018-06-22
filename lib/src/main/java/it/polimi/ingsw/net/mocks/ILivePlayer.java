package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface ILivePlayer extends JSONSerializable {
    @JSONElement("favour-tokens")
    int getFavourTokens();

    @JSONElement("window")
    IWindow getWindow();

    @JSONElement("player")
    IPlayer getPlayer();
}
