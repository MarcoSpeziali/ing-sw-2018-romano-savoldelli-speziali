package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.rmi.Remote;

public interface IResult extends JSONSerializable, Remote {
    IPlayer getPlayer();
    IMatch getMatch();
    int getPoints();
}
