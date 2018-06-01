package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// TODO: docs
public class Body extends HashMap<String, Object> implements JSONSerializable {

    private static final long serialVersionUID = 3489527493830667316L;

    public Body() {}

    public Body(EndPointFunction endPointFunction) {
        this.setEndPointFunction(endPointFunction);
    }

    public Body(Map<String, Object> map) {
        this.putAll(map);
    }

    public Body(EndPointFunction endPointFunction, Map<String, Object> map) {
        this.setEndPointFunction(endPointFunction);
        this.putAll(map);
    }

    public EndPointFunction getEndPointFunction() {
        String endpoint = (String) this.getOrDefault("endpoint", null);

        if (endpoint == null) {
            return null;
        }

        return EndPointFunction.fromEndPointFunctionName(endpoint);
    }

    public void setEndPointFunction(EndPointFunction endPointFunction) {
        this.put("endpoint", endPointFunction.getEndPointFunctionName());
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            this.put(key, jsonObject.get(key));
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        for (Entry entry : this.entrySet()) {
            jsonObject.put((String) entry.getKey(), entry.getValue());
        }

        return jsonObject;
    }
}
