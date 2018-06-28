package it.polimi.ingsw.net;

import it.polimi.ingsw.net.responses.NullResponse;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONRootElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents a response to a request.
 */
@JSONRootElement(value = "response", isOptional = true)
public class Response<T extends JSONSerializable> implements JSONSerializable {

    private static final long serialVersionUID = -3163357263339164222L;

    public static final Response<NullResponse> NULL = new Response<>(
            new Header(null, null, null),
            new NullResponse()
    );

    /**
     * The header of the response.
     */
    @JSONElement("header")
    private Header header;

    /**
     * The body of the response.
     */
    @JSONElement("body")
    private T body;

    /**
     * The error (if any) of the response.
     */
    @JSONElement("error")
    private ResponseError error;

    public Response(Header header, T body, ResponseError error) {
        this.header = header;
        this.body = body;
        this.error = error;
    }

    @JSONDesignatedConstructor
    Response(
            @JSONElement("header") Header requestHeader,
            @JSONElement("error") ResponseError error,
            @JSONElement(value = "body", keepRaw = true) JSONObject responseBody
    ) throws ClassNotFoundException {
        this.header = requestHeader;
        
        if (responseBody != null) {
            //noinspection unchecked
            this.body = JSONSerializable.deserialize(
                    (Class<T>) Class.forName(responseBody.getString(ResponseFields.Body.CLASS_TYPE.toString())),
                    responseBody
            );
        }
        
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
    public JSONObject serialize() {
        JSONObject jsonObject = JSONSerializable.super.serialize();

        if (this.body != null) {
            jsonObject
                    .getJSONObject(ResponseFields.RESPONSE.toString())
                    .getJSONObject(ResponseFields.BODY.toString())
                    .put(ResponseFields.Body.CLASS_TYPE.toString(), this.body.getClass().getName());
        }

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
