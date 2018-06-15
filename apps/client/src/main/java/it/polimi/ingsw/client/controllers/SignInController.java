package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.net.authentication.SignInManager;
import it.polimi.ingsw.net.utils.ResponseFields;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.function.Consumer;

public class SignInController {

    public void onSignInRequested(String username, String password, Runnable onCompletion, Consumer<ResponseFields.Error> onError) {
        try {
            if (SignInManager.getManager().signIn(username, password)) {
                if (onCompletion != null) {
                    onCompletion.run();
                }
            }
            else {
                if (onError != null) {
                    onError.accept(ResponseFields.Error.UNAUTHORIZED);
                }
            }
        }
        catch (IOException | NotBoundException e) {
            if (onError != null) {
                onError.accept(ResponseFields.Error.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
