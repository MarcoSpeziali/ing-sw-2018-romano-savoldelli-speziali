package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class LobbyControllerRequest implements JSONSerializable {
    
    private static final long serialVersionUID = -2138960637057393764L;

    @Override
    public void deserialize(JSONObject jsonObject) {
        // not fields to deserialize
    }

    @Override
    public JSONObject serialize() {
        return new JSONObject();
    }
}
