package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.utils.ResponseFields;

import java.util.Map;

// TODO: docs
public final class ResponseFactory {
    private ResponseFactory() {}

    public static Response createAuthenticationChallengeResponse(Request originalRequest, String challenge, int sessionId) {
        return new Response(
                new Body(
                        originalRequest.getRequestBody().getEndPointFunction(),
                        Map.of(
                                ResponseFields.Authentication.CHALLENGE.getFieldName(), challenge,
                                ResponseFields.Authentication.SESSION_ID.getFieldName(), sessionId
                        )
                )
        );
    }

    public static Response createAuthenticationTokenResponse(Request originalRequest, String token) {
        return new Response(
                new Body(
                        originalRequest.getRequestBody().getEndPointFunction(),
                        Map.of(
                                ResponseFields.Authentication.TOKEN.getFieldName(), token
                        )
                )
        );
    }

    public static Response createUserCreatedResponse(Request originalRequest) {
        return new Response(
                new Body(
                        originalRequest.getRequestBody().getEndPointFunction(),
                        Map.of(
                                ResponseFields.Authentication.CREATED.getFieldName(), ""
                        )
                )
        );
    }

    public static Response createInternalServerError() {
        return new Response(
                new ResponseError(
                        ResponseFields.Error.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseFields.Error.INTERNAL_SERVER_ERROR.getName()
                )
        );
    }

    public static Response createUnauthorisedError() {
        return new Response(
                new ResponseError(
                        ResponseFields.Error.UNAUTHORIZED.getCode(),
                        ResponseFields.Error.UNAUTHORIZED.getName()
                )
        );
    }

    public static Response createAuthenticationTimeoutError() {
        return new Response(
                new ResponseError(
                        ResponseFields.Error.TIMEOUT.getCode(),
                        ResponseFields.Error.TIMEOUT.getName()
                )
        );
    }

    public static Response createAlreadyExistsError() {
        return new Response(
                new ResponseError(
                        ResponseFields.Error.ALREADY_EXISTS.getCode(),
                        ResponseFields.Error.ALREADY_EXISTS.getName()
                )
        );
    }
}
