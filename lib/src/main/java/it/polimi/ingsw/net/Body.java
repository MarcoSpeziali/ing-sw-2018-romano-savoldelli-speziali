package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the endpoint function and the endpoint-specific arguments.
 */
public class Body extends HashMap<String, Object> implements JSONSerializable {

    private static final long serialVersionUID = 3489527493830667316L;

    /**
     * Creates an empty {@link Body}.
     */
    public Body() {}

    /**
     * Creates a {@link Body} with an endpoint and no arguments.
     * @param endPointFunction the endpoint
     */
    public Body(EndPointFunction endPointFunction) {
        this.setEndPointFunction(endPointFunction);
    }

    /**
     * Creates a {@link Body} with an endpoint and arguments.
     * @param endPointFunction the endpoint
     * @param map a {@link Map} containing the arguments
     */
    public Body(EndPointFunction endPointFunction, Map<String, Object> map) {
        this.setEndPointFunction(endPointFunction);
        this.putAll(map);
    }

    /**
     * Returns the endpoint function.
     * @return the endpoint function
     */
    public EndPointFunction getEndPointFunction() {
        String endpoint = (String) this.getOrDefault("endpoint", null);

        if (endpoint == null) {
            return null;
        }

        return EndPointFunction.fromEndPointFunctionName(endpoint);
    }

    /**
     * Sets the endpoint function.
     * @param endPointFunction the endpoint function
     */
    public void setEndPointFunction(EndPointFunction endPointFunction) {
        if (endPointFunction == null) {
            this.remove("endpoint");
        }
        else {
            this.put("endpoint", endPointFunction.getEndPointFunctionName());
        }
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

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
