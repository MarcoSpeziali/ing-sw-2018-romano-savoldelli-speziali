package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class SignInController {

    public void onSignInRequested(String username, String password, Runnable onCompletion, Consumer<ResponseFields.Error> onError) {
        Task<ResponseFields.Error> signInTask = new Task<>() {
            @Override
            protected ResponseFields.Error call() throws Exception {
                return SignInManager.getManager().signIn(username, password);
            }
        };
        signInTask.setOnSucceeded(event -> {
            ResponseFields.Error response = signInTask.getValue();

            if (onCompletion != null && response == null) {
                onCompletion.run();
            }
            else if (onError != null && response.getCode() == ResponseFields.Error.UNAUTHORIZED.getCode()) {
                onError.accept(ResponseFields.Error.UNAUTHORIZED);
            }
            else if (onError != null && response.getCode() == ResponseFields.Error.ALREADY_EXISTS.getCode()) {
                onError.accept(ResponseFields.Error.ALREADY_EXISTS);
            }
        });
        signInTask.setOnFailed(event -> {
            if (onError != null) {
                onError.accept(ResponseFields.Error.INTERNAL_SERVER_ERROR);
            }
        });
        new Thread(signInTask).start();
    }
}
