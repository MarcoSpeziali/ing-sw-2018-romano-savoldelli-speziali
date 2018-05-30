package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RemoteFunction;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 5889311626900785609L;

    private RemoteFunction remoteFunctionName;

    public Request() {

    }
}
