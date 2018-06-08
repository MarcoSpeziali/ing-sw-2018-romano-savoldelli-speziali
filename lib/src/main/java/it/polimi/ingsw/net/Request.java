package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.utils.ReflectionUtils;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents a socket request.
 */
public class Request<T extends JSONSerializable> implements JSONSerializable {
    private static final long serialVersionUID = 5889311626900785609L;

    /**
     * The request header.
     */
    private Header header;

    /**
     * The request body.
     */
    private T body;

    /**
     * @return the request header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the request header.
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
     * @param requestBody the request body
     */
    public void setBody(T requestBody) {
        this.body = requestBody;
    }

    public Request() { }

    public Request(Header requestHeader, T requestBody) {
        this.header = requestHeader;
        this.body = requestBody;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        jsonObject = jsonObject.getJSONObject(RequestFields.REQUEST.toString());

        this.header = new Header();
        this.header.deserialize(jsonObject.getJSONObject(RequestFields.HEADER.toString()));

        try {
            this.body = ReflectionUtils.instantiateGenericParameter(this.getClass());
        }
        catch ( NoSuchMethodException       |
                InstantiationException      |
                InvocationTargetException   |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.body.deserialize(jsonObject.getJSONObject(RequestFields.BODY.toString()));
    }

    @Override
    public JSONObject serialize() {
        JSONObject mainJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(
                RequestFields.HEADER.toString(),
                this.header.serialize()
        );
        jsonObject.put(
                RequestFields.BODY.toString(),
                this.body.serialize()
        );

        mainJsonObject.put(RequestFields.REQUEST.toString(), jsonObject);

        return mainJsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
