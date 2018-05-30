package it.polimi.ingsw.server.net.authentication;

import it.polimi.ingsw.net.AuthenticationResponse;
import it.polimi.ingsw.net.authentication.AuthenticationInterface;

public class LoginManager implements AuthenticationInterface {

    @Override
    public AuthenticationResponse requestLogin(String username) {
        String randomString = "..";
        String userPassword = ".."; // preso da db in base a username

        String expectedResult = "hash(randomString, userPassword)"; // salvare su database(username)

        return new AuthenticationResponse(); // randomString
    }

    @Override
    public AuthenticationResponse fulfillChallenge(String sessionId, String challengeResponse) {
        String expectedResult = ".."; // preso da db (sessionId)

        if (expectedResult.equals(challengeResponse)) {
            String token = ".."; // f(sessionId) unique
            return new AuthenticationResponse(); // token
        }
        else {
            return new AuthenticationResponse(); // error
        }
    }
}
