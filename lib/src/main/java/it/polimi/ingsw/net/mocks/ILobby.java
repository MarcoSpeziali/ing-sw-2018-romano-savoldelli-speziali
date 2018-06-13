package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.rmi.Remote;
import java.util.List;
import java.util.stream.Collectors;

// TODO: docs
public interface ILobby extends JSONSerializable, Remote {
    int getId();

    long getOpeningTime();

    long getClosingTime();

    List<IPlayer> getPlayers();

    @Override
    default JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.getId());
        jsonObject.put("opening-time", this.getOpeningTime());
        jsonObject.put("closing-time", this.getClosingTime());
        jsonObject.put("players", this.getPlayers().stream().map(JSONSerializable::serialize).collect(Collectors.toList()));

        return jsonObject;
    }
}
