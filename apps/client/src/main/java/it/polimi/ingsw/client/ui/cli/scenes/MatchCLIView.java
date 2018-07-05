package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.client.ui.cli.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchCLIView extends CLIView<MatchController> {


    private ToolCardCLIView[] toolCardCLIViews;
    private ObjectiveCardCLIView[] objectiveCardCLIViews;
    private ObjectiveCardCLIView objectiveCardCLIView;
    private RoundTrackCLIView roundTrackCLIView;
    private DraftPoolCLIView draftPoolCLIView;
    private WindowCLIView[] windowCLIViews;
    private WindowCLIView windowCLIView;

    private IWindow iWindow;
    private IToolCard[] iToolCards;

    Scanner scanner = new Scanner(System.in);

    private final Object updateSyncObject = new Object();
    private ExecutorService matchExecutorService = Executors.newFixedThreadPool(10);

    private ILivePlayer currentPlayer;


    @Override
    public void setModel(MatchController matchController) {
        this.model = matchController;
    }

    private void setUpWaitForTurnToEnd() {
        this.matchExecutorService.submit((Callable<Void>) () -> {
            this.model.waitForTurnToEnd();

            Match.performedAction = 0;

            setUpWaitForTurnToBegin();

            return null;
        });
    }

    private void setUpWaitForTurnToBegin() {
        this.matchExecutorService.submit((Callable<Void>) () -> {

            Match.performedAction = 0b00;

            Platform.runLater(unsafe(() -> {
                System.out.println("It's your turn!");
                System.out.println("Type 'move' to pick/put a die, 'toolcard' to use a toolcard 'check' to pass turn");

                switch (scanner.nextLine()) {
                    case "move" : {
                        if ((Match.performedAction & 0b01) != 0b01) {
                            this.move();
                            break;
                        }
                        else System.out.println("You cannot move twice!");
                    }
                    case "toolcard" : {
                        if ((Match.performedAction & 0b01) != 0b01) {
                            this.toolCard();
                            break;
                        }
                        else System.out.println("You cannot use a Tool Card twice!");
                    }
                    case "check" : {
                        System.out.println("Passing turn");
                    }
                }

            }));
            setUpWaitForTurnToEnd();

            return null;
        });
    }

    @Override
    public void init() {
        chooseWindow();
        render();

        setUpWaitForTurnToBegin();
        setUpWaitForMatchToEnd();
    }

    private void setUpWaitForMatchToEnd() {
        this.matchExecutorService.submit((Callable<Void>) () -> {
            IResult[] results = Arrays.stream(this.model.waitForMatchToEnd())
                    .sorted(Comparator.comparing(IResult::getPoints))
                    .toArray(IResult[]::new);

            try {
                Platform.runLater(wrap(() -> {

                    String currentPlayerName = this.currentPlayer.getPlayer().getUsername();

                    for (IResult result : results) {
                        System.out.println(result.getPlayer().getUsername() + " " + result.getPoints());
                    }

                    if (results[0].getPlayer().getUsername().equals(currentPlayerName)) {
                        System.out.println("Congrats! You won the game");
                    }
                }));
            }
            catch (FunctionalExceptionWrapper e) {
                e.tryFinalUnwrap(IOException.class);
            }

            return null;
        });

    }

    private void chooseWindow() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForWindowRequest()))
                .thenAccept(iWindows -> {
                    Platform.runLater(unsafe(() -> {
                        for (int i = 0; i < iWindows.length; i++) {
                            System.out.println("Window #"+i);
                            WindowCLIView windowCLIView = new WindowCLIView();
                            windowCLIView.setModel(iWindows[i]);
                            windowCLIView.render();
                        }
                        System.out.println("Choose Window (0:4)");

                        int cmd = scanner.nextInt();

                        this.model.respondToWindowRequest(iWindows[cmd]);
                    }));
                });
    }

    @Override
    public void render()  {
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

    private void move() {


        System.out.println("Command usage: [draftpool location] [row/column coordinate]");

            String command = scanner.nextLine();
            String[] locations = command.split(" ");

            Move move = Move.build();
            move.begin(Integer.parseInt(locations[0]));

            int endLocation = (Integer.parseInt(locations[1].split("(?!^)")[0])) *
                    this.iWindow.getColumns() +
                    (Integer.parseInt(locations[1].split("(?!^)")[0]));
            move.end(endLocation);
            try {
                MoveResponse moveResponse = this.model.tryToMove(move);

                if (!moveResponse.isValid()) {
                    System.out.println("Your move is not valid");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }

    private void toolCard() {
        System.out.println("Choose toolCard index (0:2)");

        int index = scanner.nextInt();
        try {
            this.model.requestToolCardUsage(iToolCards[index]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.println("You do not have enough tokens!");
        }

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

                this.currentPlayer = iMatch.getCurrentPlayer();

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
            System.out.println("ToolCard #"+i);
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
        System.out.println("Opponents' Windows");

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
