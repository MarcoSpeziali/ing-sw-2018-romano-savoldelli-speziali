package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.net.authentication.SignUpManager;
import it.polimi.ingsw.net.utils.ResponseFields;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.security.GeneralSecurityException;
import java.util.function.Consumer;

public class SignUpController {
    public void onSignUpRequested(String username, String password, Runnable onCompletion, Consumer<ResponseFields.Error> onError) {
        try {
            if (SignUpManager.getManager().signUp(username, password)) {
                if (onCompletion != null) {
                    onCompletion.run();
                }
            }
            else {
                if (onError != null) {
                    onError.accept(ResponseFields.Error.ALREADY_EXISTS);
                }
            }
        }
        catch (IOException | GeneralSecurityException | NotBoundException e) {
            e.printStackTrace();

            if (onError != null) {
                onError.accept(ResponseFields.Error.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
