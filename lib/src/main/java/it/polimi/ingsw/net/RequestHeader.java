package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents the header of the request.
 */
public class RequestHeader implements JSONSerializable {
    private static final long serialVersionUID = -5858895441251305795L;

    /**
     * The user's machine info.
     */
    private ClientMachineInfo clientMachine;

    /**
     * The user's token.
     */
    private String clientToken;

    /**
     * @return the user's machine info
     */
    public ClientMachineInfo getClientMachine() {
        return clientMachine;
    }

    /**
     * Test the user's machine info.
     * @param clientMachine the user's machine info
     */
    public void setClientMachine(ClientMachineInfo clientMachine) {
        this.clientMachine = clientMachine;
    }

    /**
     * @return the user's token
     */
    public String getClientToken() {
        return clientToken;
    }

    /**
     * Sets the user's token.
     * @param clientToken the user's token
     */
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
        if (jsonObject.has("client-machine")) {
            ClientMachineInfo clientMachineInfo = new ClientMachineInfo();
            clientMachineInfo.deserialize(jsonObject.getJSONObject("client-machine"));
        }

        if (jsonObject.has("client-token")) {
            this.clientToken = jsonObject.getString("client-token");
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("client-machine", this.clientMachine == null ? new JSONObject() : this.clientMachine.serialize());
        jsonObject.put("client-token", this.clientToken);

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
