package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class SignInController {

    public void onSignInRequested(String username, String password, Runnable onCompletion, Consumer<ResponseFields.Error> onError) {
        Task<Boolean> signInTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return SignInManager.getManager().signIn(username, password);
            }
        };
        signInTask.setOnSucceeded(event -> {
            boolean signedIn = signInTask.getValue();

            if (onCompletion != null && signedIn) {
                onCompletion.run();
            }
            else if (onError != null && !signedIn) {
                onError.accept(ResponseFields.Error.UNAUTHORIZED);
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
