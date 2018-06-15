package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.io.FilesUtils;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.utils.text.CypherUtils;
import it.polimi.ingsw.utils.text.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignUpEndPointTest {

    @Test
    void testRequestSignUpOnAlreadyExistingUser() throws IOException, ParserConfigurationException, SAXException {
        Socket socket = mock(Socket.class);
        SocketAddress socketAddress = mock(SocketAddress.class);
        when(socketAddress.toString()).thenReturn("/1.2.3.4:1234");
        when(socket.getRemoteSocketAddress()).thenReturn(socketAddress);

        SignUpEndPoint signUpEndPoint = new SignUpEndPoint();
        Response<SignUpResponse> signUpResponse = signUpEndPoint.requestSignUp(new Request<>(
                new Header(EndPointFunction.SIGN_UP),
                new SignUpRequest(
                        "sagrada",
                        "..."
                )
        ));

        Assertions.assertEquals(ResponseFields.Error.ALREADY_EXISTS.getCode(), signUpResponse.getError().getErrorCode());
    }

    @Test
    void testRequestSignUp() throws ParserConfigurationException, SAXException, IOException, GeneralSecurityException, SQLException {
        Socket socket = mock(Socket.class);
        SocketAddress socketAddress = mock(SocketAddress.class);
        when(socketAddress.toString()).thenReturn("/1.2.3.4:1234");
        when(socket.getRemoteSocketAddress()).thenReturn(socketAddress);

        SignUpEndPoint signUpEndPoint = new SignUpEndPoint();
        Response<SignUpResponse> signUpResponse = signUpEndPoint.requestSignUp(new Request<>(
                new Header(EndPointFunction.SIGN_UP),
                new SignUpRequest(
                        "test",
                        CypherUtils.encryptString(
                                "test_password",
                                FilesUtils.getFileContentAsBytes(Resources.getResource(
                                        SignUpEndPointTest.class.getClassLoader(),
                                        "public.der")
                                ),
                                false
                        )
                )
        ));

        Assertions.assertNull(signUpResponse.getError());

        DatabasePlayer databasePlayer = DatabasePlayer.playerWithUsername("test");

        Assertions.assertNotNull(databasePlayer);
        Assertions.assertEquals("test", databasePlayer.getUsername());
        Assertions.assertEquals(HashUtils.sha1("test_password"), databasePlayer.getPassword());
    
        DatabasePlayer.deletePlayer(databasePlayer.getId());
    }
}