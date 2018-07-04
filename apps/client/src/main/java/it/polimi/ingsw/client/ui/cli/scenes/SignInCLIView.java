package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.controllers.SignInController;
import it.polimi.ingsw.client.controllers.proxies.socket.LobbySocketProxyController;
import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.application.Platform;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class SignInCLIView {

    static Scanner reader = new Scanner(System.in);
    private static String username;
    private static String password;

    public static void render() {

        String selection;
        System.out.println("SignIn screen. Type --back to go back");

        do {

            System.out.print("username: ");
            selection = reader.nextLine();
            username = selection;
            if (username.equals("--back")) break;

            System.out.print("password: ");
            selection = reader.nextLine();
            password = selection;
            if (password.equals("--back")) break;

               SignInController signInController = new SignInController();
               signInController.onSignInRequested(username, password, () -> {
                   try {
                       LobbyCLIView.render();
                       LobbyController lobbyController;

                       if (Settings.getSettings().getProtocol() == Constants.Protocols.SOCKETS) {
                           lobbyController = new LobbySocketProxyController(
                                   Settings.getSettings().getServerAddress(),
                                   Settings.getSettings().getServerSocketPort(),
                                   SignInManager.getManager().getToken()
                           );
                       } else {
                           OneTimeRMIResponseProvider<LobbyInterface> oneTimeRMIResponseProvider = new OneTimeRMIResponseProvider<>(
                                   Settings.getSettings().getServerAddress(),
                                   Settings.getSettings().getServerRMIPort(),
                                   LobbyInterface.class
                           );

                           lobbyController = oneTimeRMIResponseProvider.getSyncRemoteObject(EndPointFunction.LOBBY_JOIN_REQUEST_RMI, SignInManager.getManager().getToken());
                       }


                   } catch (RemoteException | NotBoundException e) {
                       e.printStackTrace();
                   }
               }, responseError -> {
                   String error;
                  if (responseError == ResponseFields.Error.UNAUTHORIZED) {
                      error = Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ACCESS_DENIED_CONTEXT_TEXT);
                  }
                  else if (responseError == ResponseFields.Error.ALREADY_EXISTS) {
                      error = Constants.Strings.toLocalized(Constants.Strings.SIGN_IN_ALREADY_EXISTS_CONTEXT_TEXT);
                  }
                  else {
                      error = Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT);
                  }
                  System.err.println(error);

               });



        } while (true);

        System.out.println("Back to main menu");
    }
}
