package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.util.Map;

public class SignUpResponse implements JSONSerializable {

    private static final long serialVersionUID = 5399125031412209261L;

    public static final String CREATED_FIELD = "created";

    @Override
    public void deserialize(JSONObject jsonObject) {
        // no fields needed
    }

    @Override
    public JSONObject serialize() {
        return new JSONObject(Map.of(CREATED_FIELD, true));
    }
}
