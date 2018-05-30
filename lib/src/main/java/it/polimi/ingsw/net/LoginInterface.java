package it.polimi.ingsw.net;

import java.util.function.Consumer;
import java.util.function.Function;

public interface LoginInterface {
    void requestLogin(Function<String, String> challengeResponse, Consumer<String> tokenResponse, Consumer<String> authError);
}