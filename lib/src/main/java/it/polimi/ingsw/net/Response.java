package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents a response to a request.
 */
public class Response<T extends JSONSerializable> implements JSONSerializable {

    private static final long serialVersionUID = -3163357263339164222L;

    /**
     * The header of the response.
     */
    private Header header;

    /**
     * The body of the response.
     */
    private T body;

    /**
     * The error (if any) of the response.
     */
    private ResponseError error;

    public Response() {

    }

    public Response(Header header, T body, ResponseError error) {
        this.header = header;
        this.body = body;
        this.error = error;
    }

    public Response(Header header, T body) {
        this(header, body, null);
    }

    public Response(Header header, ResponseError error) {
        this(header, null, error);
    }

    /**
     * @return the header of the response
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @return the body of the response
     */
    public T getBody() {
        return body;
    }

    /**
     * @return the error (if any) of the response
     */
    public ResponseError getError() {
        return error;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if (jsonObject.has(ResponseFields.RESPONSE.toString())) {
            jsonObject = jsonObject.getJSONObject(ResponseFields.RESPONSE.toString());
        }

        this.header = new Header();
        this.header.deserialize(jsonObject.getJSONObject(RequestFields.HEADER.toString()));

        if (jsonObject.has(ResponseFields.BODY.toString())) {
            JSONObject bodyObject = jsonObject.getJSONObject(ResponseFields.BODY.toString());

            try {
                this.body = this.instantiateBody(bodyObject.getString(ResponseFields.Body.CLASS_TYPE.toString()));
            }
            catch (NoSuchMethodException |
                    InstantiationException |
                    InvocationTargetException |
                    ClassNotFoundException |
                    IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            this.body.deserialize(jsonObject.getJSONObject(ResponseFields.BODY.toString()));
        }

        if (jsonObject.has(ResponseFields.ERROR.toString())) {
            this.error = new ResponseError();
            this.error.deserialize(jsonObject.getJSONObject(ResponseFields.ERROR.toString()));
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject mainJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ResponseFields.HEADER.toString(), this.header.serialize());

        if (this.error != null) {
            jsonObject.put(
                    ResponseFields.ERROR.toString(),
                    this.error.serialize()
            );
        }

        if (this.body != null) {
            JSONObject serializedBody = this.body.serialize();
            serializedBody.put(ResponseFields.Body.CLASS_TYPE.toString(), this.body.getClass().getName());

            jsonObject.put(
                    ResponseFields.BODY.toString(),
                    serializedBody
            );
        }

        mainJsonObject.put(ResponseFields.RESPONSE.toString(), jsonObject);
        return mainJsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }

    private T instantiateBody(String className) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        return (T) Class.forName(className).getConstructor().newInstance();
    }
}
