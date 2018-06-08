package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface SignUpInterface extends Remote {

    @RespondsTo(EndPointFunction.SIGN_UP)
    Response<SignUpResponse> requestSignUp(Request<SignUpRequest> request) throws RemoteException;
}
