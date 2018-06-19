package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class SignUpResponse implements JSONSerializable {

    private static final long serialVersionUID = 5399125031412209261L;

    @JSONElement("created")
    private boolean created;
}
