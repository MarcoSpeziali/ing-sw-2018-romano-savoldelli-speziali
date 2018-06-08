package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.JSONSerializable;

// TODO: docs
public final class ResponseFactory {
    private ResponseFactory() {}

    public static Response<ChallengeRequest> createAuthenticationChallengeResponse(Request<SignInRequest> originalRequest, String challenge, int sessionId) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new ChallengeRequest(challenge, sessionId)
        );
    }

    public static Response<SignInResponse> createAuthenticationTokenResponse(Request<ChallengeResponse> originalRequest, String token) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new SignInResponse(token)
        );
    }

    public static Response<SignUpResponse> createUserCreatedResponse(Request<SignUpRequest> originalRequest) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new SignUpResponse()
        );
    }

    public static <T extends JSONSerializable> Response<T> createInternalServerError(Request<?> originalRequest) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new ResponseError(
                        ResponseFields.Error.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseFields.Error.INTERNAL_SERVER_ERROR.getName()
                )
        );
    }

    public static <T extends JSONSerializable> Response<T> createUnauthorisedError(Request<?> originalRequest) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new ResponseError(
                        ResponseFields.Error.UNAUTHORIZED.getCode(),
                        ResponseFields.Error.UNAUTHORIZED.getName()
                )
        );
    }

    public static <T extends JSONSerializable> Response<T> createAuthenticationTimeoutError(Request<?> originalRequest) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new ResponseError(
                        ResponseFields.Error.TIMEOUT.getCode(),
                        ResponseFields.Error.TIMEOUT.getName()
                )
        );
    }

    public static <T extends JSONSerializable> Response<T> createAlreadyExistsError(Request<?> originalRequest) {
        return new Response<>(
                new Header(
                        originalRequest.getHeader().getEndPointFunction()
                ),
                new ResponseError(
                        ResponseFields.Error.ALREADY_EXISTS.getCode(),
                        ResponseFields.Error.ALREADY_EXISTS.getName()
                )
        );
    }
}
