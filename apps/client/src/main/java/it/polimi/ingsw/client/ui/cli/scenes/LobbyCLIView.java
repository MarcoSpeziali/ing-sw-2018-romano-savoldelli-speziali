package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


public class LobbyCLIView {
    // TODO timer: think about it...

    private static LobbyController proxyController;

    public static void setProxy(LobbyController controller) {
        render();
        LobbyCLIView.proxyController = controller;
        unsafe(() -> {
            controller.init();
            setUpUpdateFuture();
            setUpMatchFuture();
        }).run();
    }

    private static void setUpMatchFuture() {
    }

    private static void setUpUpdateFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> proxyController.waitForUpdate()))
                .thenAccept(LobbyCLIView::onUpdateReceived);
    }


    private static void onUpdateReceived(ILobby update){
        Platform.runLater(() -> {
            System.out.println("Update #" + update.getId() + " List of players:\n");

            for (int i = 0; i < update.getPlayers().length; i++) {
                IPlayer player = update.getPlayers()[i];
                System.out.println(player.getUsername());
        }});
        setUpUpdateFuture();
    }


    public static void render() {
        System.out.println("Wait for other players to connect.");
    }
}
