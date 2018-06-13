package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

// TODO: docs
public interface IPlayer extends JSONSerializable {
    int getId();

    String getUsername();

    @Override
    default JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.getId());
        jsonObject.put("username", this.getUsername());

        return jsonObject;
    }
}
