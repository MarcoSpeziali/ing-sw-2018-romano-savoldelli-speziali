package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.controllers.proxies.socket.LobbySocketProxyController;
import it.polimi.ingsw.client.controllers.proxies.socket.MatchSocketProxyController;
import it.polimi.ingsw.client.net.SignInManager;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.client.ui.gui.scenes.MatchGUIView;
import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.responses.MigrationResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import javafx.application.Platform;
import javafx.scene.Parent;

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
        CompletableFuture.supplyAsync(unsafe(() -> LobbyCLIView.proxyController.waitForMigrationRequest()))
                .thenAccept(iMatch -> Platform.runLater(unsafe(() -> {

                    MatchController matchController;

                    if (Settings.getSettings().getProtocol() == Constants.Protocols.RMI) {
                        OneTimeRMIResponseProvider<MatchInterface> oneTimeRMIResponseProvider = new OneTimeRMIResponseProvider<>(
                                Settings.getSettings().getServerAddress(),
                                Settings.getSettings().getServerRMIPort(),
                                MatchInterface.class
                        );

                        matchController = oneTimeRMIResponseProvider.getSyncRemoteObject(EndPointFunction.MATCH_MIGRATION_RMI, new Response<>(
                                new Header(
                                        SignInManager.getManager().getToken(),
                                        EndPointFunction.MATCH_MIGRATION
                                ),
                                new MigrationResponse(iMatch.getId())
                        ));
                    }
                    else {
                        matchController = new MatchSocketProxyController(
                                ((LobbySocketProxyController) LobbyCLIView.proxyController).getPersistentSocketInteractionProvider(),
                                SignInManager.getManager().getToken()
                        );
                    }

                    matchController.init(iMatch.getId());

                    MatchCLIView matchCLIView = new MatchCLIView();
                    matchCLIView.setModel(matchController);
                    matchCLIView.init();
                    Match.setMatchController(matchController);

                })));
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
