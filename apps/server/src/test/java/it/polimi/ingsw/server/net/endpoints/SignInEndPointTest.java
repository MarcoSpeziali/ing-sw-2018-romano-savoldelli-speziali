package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.utils.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignInEndPointTest {

    @Test
    void testSignIn() throws IOException, ParserConfigurationException, SAXException {
        Settings.build();

        Socket socket = mock(Socket.class);
        SocketAddress socketAddress = mock(SocketAddress.class);
        when(socketAddress.toString()).thenReturn("/1.2.3.4:1234");
        when(socket.getRemoteSocketAddress()).thenReturn(socketAddress);

        SignInEndPoint signInEndPoint = new SignInEndPoint();
        signInEndPoint.setSocket(socket);

        Response<ChallengeRequest> challengeRequest = signInEndPoint.requestSignIn(new Request<>(
                new Header(EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION),
                new SignInRequest("sagrada")
        ));

        Assertions.assertNull(challengeRequest.getError());

        Response<SignInResponse> signInResponse = signInEndPoint.fulfillChallenge(new Request<>(
                new Header(EndPointFunction.SIGN_IN_FULFILL_CHALLENGE),
                new ChallengeResponse(
                        HashUtils.sha1(
                                challengeRequest.getBody().getChallenge() +
                                        HashUtils.sha1("sagrada")
                        ),
                        challengeRequest.getBody().getSessionId()
                )
        ));

        Assertions.assertNull(signInResponse.getError());
    }
}