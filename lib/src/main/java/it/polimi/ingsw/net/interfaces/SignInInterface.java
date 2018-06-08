package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface SignInInterface extends Remote {

    @RespondsTo(EndPointFunction.REQUEST_AUTHENTICATION)
    Response<ChallengeRequest> requestLogin(Request<SignInRequest> request) throws IOException;

    @RespondsTo(EndPointFunction.FULFILL_AUTHENTICATION_CHALLENGE)
    Response<SignInResponse> fulfillChallenge(Request<ChallengeResponse> request) throws RemoteException;
}