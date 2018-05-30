package it.polimi.ingsw.server.net.authentication;

import it.polimi.ingsw.net.interfaces.LoginInterface;

public class LoginManager implements LoginInterface {

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
