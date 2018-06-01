package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

// TODO: docs
public class RequestHeader implements JSONSerializable {
    private static final long serialVersionUID = -5858895441251305795L;

    private ClientMachineInfo clientMachine;
    private String clientToken;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ClientMachineInfo getClientMachine() {
        return clientMachine;
    }

    public void setClientMachine(ClientMachineInfo clientMachine) {
        this.clientMachine = clientMachine;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public RequestHeader() { }

    public RequestHeader(ClientMachineInfo clientMachine, String clientToken) {
        this.clientMachine = clientMachine;
        this.clientToken = clientToken;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        ClientMachineInfo clientMachineInfo = new ClientMachineInfo();
        clientMachineInfo.deserialize(jsonObject.getJSONObject("client-machine"));

        this.clientToken = jsonObject.getString("client-token");
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("client-machine", this.clientMachine.serialize());
        jsonObject.put("client-token", this.clientToken);

        return jsonObject;
    }
}
