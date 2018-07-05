package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.ui.cli.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.net.mocks.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class MatchCLIView extends CLIView<MatchController> {


    private ToolCardCLIView[] toolCardCLIViews;
    private ObjectiveCardCLIView[] objectiveCardCLIViews;
    private ObjectiveCardCLIView objectiveCardCLIView;
    private RoundTrackCLIView roundTrackCLIView;
    private DraftPoolCLIView draftPoolCLIView;
    private WindowCLIView[] windowCLIViews;
    private WindowCLIView windowCLIView;

    private final Object updateSyncObject = new Object();
    private ExecutorService matchExecutorService = Executors.newFixedThreadPool(10);


    @Override
    public void setModel(MatchController matchController) {
        this.model = matchController;
    }





















    @Override
    public void render() {
        matchExecutorService.submit(() -> {
            synchronized (updateSyncObject) {
                while (true) {
                    IMatch match = this.model.waitForUpdate();
                    loadElements(match);

                    updateSyncObject.wait();
                }
            }
        });
    }

    private void loadElements(IMatch iMatch) {
        Platform.runLater(unsafe(() -> {
            synchronized (updateSyncObject) {
                loadRoundTrack(iMatch.getRoundTrack());
                loadDraftPool(iMatch.getDraftPool());
                loadToolCards(iMatch.getToolCards());
                loadObjectiveCards(iMatch.getPublicObjectiveCards(), iMatch.getPrivateObjectiveCard());
                loadOpponentWindows(iMatch.getPlayers());
                loadOwnedWindow(iMatch.getCurrentPlayer().getWindow());

                updateSyncObject.notifyAll();
            }
        }));
    }


    private void loadToolCards(IToolCard[] toolCards) {

        if (toolCardCLIViews == null) {
            toolCardCLIViews = new ToolCardCLIView[toolCards.length];
        }
        System.out.println("ToolCards");

        for (int i = 0; i < toolCards.length; i++) {
            toolCardCLIViews[i].setModel(toolCards[i]);
            toolCardCLIViews[i].render();
            System.out.println();
        }

    }

    private void loadObjectiveCards(IObjectiveCard[] objectiveCards, IObjectiveCard objectiveCard) throws IOException {

        if (objectiveCardCLIViews == null) {
            objectiveCardCLIViews = new ObjectiveCardCLIView[objectiveCards.length];
        }
        System.out.println("Public objective cards");

        for (int i = 0; i < objectiveCards.length; i++) {
            objectiveCardCLIViews[i].setModel(objectiveCards[i]);
            objectiveCardCLIViews[i].render();
            System.out.println();
        }

        System.out.println("Private objective cards");
        if (objectiveCardCLIView == null) objectiveCardCLIView = new ObjectiveCardCLIView();

        objectiveCardCLIView.setModel(objectiveCard);
        objectiveCardCLIView.render();
        System.out.println();
    }

    private void loadRoundTrack(IRoundTrack iRoundTrack) throws IOException {
        System.out.println("RoundTrack");

        if (roundTrackCLIView == null) roundTrackCLIView = new RoundTrackCLIView();

        roundTrackCLIView.setModel(iRoundTrack);
        roundTrackCLIView.render();
        System.out.println();

    }

    private void loadDraftPool(IDraftPool iDraftPool) throws IOException {
        System.out.println("DraftPool");

        if (draftPoolCLIView == null) draftPoolCLIView = new DraftPoolCLIView();

        draftPoolCLIView.setModel(iDraftPool);
        draftPoolCLIView.render();
        System.out.println();

    }

    private void loadOpponentWindows(ILivePlayer[] player) throws IOException {
        System.out.println("Opponets' Windows");

        if (windowCLIViews == null) windowCLIViews = new WindowCLIView[player.length];

        for (int i = 0; i < player.length; i++) {
            System.out.println(player[i].getPlayer().getUsername());
            windowCLIViews[i].setModel(player[i].getWindow());
            windowCLIViews[i].render();
            System.out.println();
        }
    }

    private void loadOwnedWindow(IWindow iWindow) throws IOException {
        System.out.println("Your Window");

        if (windowCLIView == null) windowCLIView = new WindowCLIView();

        windowCLIView.setModel(iWindow);
        windowCLIView.render();
        System.out.println();
    }

}
