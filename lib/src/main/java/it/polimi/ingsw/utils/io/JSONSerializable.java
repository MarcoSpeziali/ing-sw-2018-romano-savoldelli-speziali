package it.polimi.ingsw.utils.io;

import org.json.JSONObject;

import java.io.Serializable;

// TODO: docs
public interface JSONSerializable extends Serializable {
    void deserialize(JSONObject jsonObject);

    JSONObject serialize();
}
