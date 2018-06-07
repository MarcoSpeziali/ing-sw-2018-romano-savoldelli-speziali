package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.JSONSerializable;

public interface IPlayer extends JSONSerializable {
    int getId();

    String getUsername();
}
