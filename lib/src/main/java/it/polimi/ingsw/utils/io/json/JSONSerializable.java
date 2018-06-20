package it.polimi.ingsw.utils.io.json;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Adds the needed function required for the JSON serialization and deserialization.
 */
public interface JSONSerializable extends Serializable {

    /**
     * Serialized the implementing class into a {@link JSONObject}.
     *
     * @return a {@link JSONObject} which represents the serialized object
     */
    default JSONObject serialize() {
        return JSONSerializationHelper.serialize(this);
    }

    // TODO: 20/06/18 document
    static <T extends JSONSerializable> T deserialize(Class<T> targetClass, String json) {
        return deserialize(targetClass, new JSONObject(json));
    }

    // TODO: 20/06/18 document
    static <T extends JSONSerializable> T deserialize(Class<T> targetClass, JSONObject jsonObject) {
        return JSONDeserializationHelper.deserialize(targetClass, jsonObject);
    }
}
