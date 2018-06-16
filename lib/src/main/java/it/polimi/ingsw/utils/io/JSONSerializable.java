package it.polimi.ingsw.utils.io;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Adds the needed function required for the JSON serialization and deserialization.
 */
public interface JSONSerializable extends Serializable {

    /**
     * Deserialized a {@link JSONObject} into the implementing class.
     *
     * @param jsonObject the {@link JSONObject} to deserialize
     */
    void deserialize(JSONObject jsonObject);

    /**
     * Serialized the implementing class into a {@link JSONObject}.
     *
     * @return a {@link JSONObject} which represents the serialized object
     */
    JSONObject serialize();
}
