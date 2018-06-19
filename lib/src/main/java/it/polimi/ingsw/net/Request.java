package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONRootElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents a socket request.
 */
@JSONRootElement(value = "request", isOptional = true)
public class Request<T extends JSONSerializable> implements JSONSerializable {
    private static final long serialVersionUID = 5889311626900785609L;

    /**
     * The request header.
     */
    @JSONElement("header")
    private Header header;

    /**
     * The request body.
     */
    @JSONElement("body")
    private T body;

    public Request() {
    }

    public Request(Header requestHeader, T requestBody) {
        this.header = requestHeader;
        this.body = requestBody;
    }

    @JSONDesignatedConstructor
    Request(
            @JSONElement("header") Header requestHeader,
            @JSONElement(value = "body", keepRaw = true) JSONObject requestBody
    ) throws ClassNotFoundException {
        this.header = requestHeader;
        //noinspection unchecked
        this.body = JSONSerializable.deserialize(
                (Class<T>) Class.forName(requestBody.getString(RequestFields.Body.CLASS_TYPE.toString())),
                requestBody
        );
    }

    /**
     * @return the request header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the request header.
     *
     * @param requestHeader the request header
     */
    public void setHeader(Header requestHeader) {
        this.header = requestHeader;
    }

    /**
     * @return the request body
     */
    public T getBody() {
        return body;
    }

    /**
     * Sets the request body.
     *
     * @param requestBody the request body
     */
    public void setBody(T requestBody) {
        this.body = requestBody;
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = JSONSerializable.super.serialize();

        jsonObject
                .getJSONObject(RequestFields.REQUEST.toString())
                .getJSONObject(RequestFields.BODY.toString())
                .put(RequestFields.Body.CLASS_TYPE.toString(), this.body.getClass().getName());

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
