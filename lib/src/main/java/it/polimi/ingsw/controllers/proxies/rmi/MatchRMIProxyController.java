package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.core.ToolCardConditionException;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.ChooseBetweenActionsRequest;
import it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest;
import it.polimi.ingsw.net.requests.SetShadeRequest;
import it.polimi.ingsw.net.requests.ShouldRepeatRequest;
import it.polimi.ingsw.net.responses.ConstraintNotMetResponse;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.net.responses.NotEnoughTokensResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class MatchRMIProxyController extends UnicastRemoteObject implements MatchController, HeartBeatListener {
    
    private static final long serialVersionUID = 5011425771999481032L;
    
    protected IPlayer player;
    private Boolean shouldBeKeptAlive;

    public MatchRMIProxyController(IPlayer player) throws RemoteException {
        super();
        
        this.player = player;
        this.shouldBeKeptAlive = true;
    }
    
    @Override
    public void init(Object... args) throws IOException {
        // useless in rmi
    }
    
    private final transient Object windowsToChooseSyncObject = new Object();
    private IWindow[] windowsToChoose;
    
    public void postWindowsToChoose(IWindow[] windows) {
        synchronized (this.windowsToChooseSyncObject) {
            this.windowsToChoose = windows;
    
            this.windowsToChooseSyncObject.notifyAll();
        }
    }
    
    @Override
    public IWindow[] waitForWindowRequest() throws RemoteException, InterruptedException {
        synchronized (this.windowsToChooseSyncObject) {
            while (this.windowsToChoose == null) {
                this.windowsToChooseSyncObject.wait();
            }
        
            return this.windowsToChoose;
        }
    }
    
    private transient Consumer<IWindow> windowResponseConsumer;
    
    public void setWindowResponseConsumer(Consumer<IWindow> windowResponseConsumer) {
        this.windowResponseConsumer = windowResponseConsumer;
    }
    
    @Override
    public void respondToWindowRequest(IWindow window) throws RemoteException {
        if (this.windowResponseConsumer != null) {

            CompletableFuture.runAsync(() -> {
                this.windowResponseConsumer.accept(window);
                this.windowResponseConsumer = null;
            });
        }
    }
    
    private final transient Object updateSyncObject = new Object();
    private IMatch update;
    
    @Override
    public IMatch waitForUpdate() throws RemoteException, InterruptedException {
        //noinspection Duplicates
        synchronized (this.updateSyncObject) {
            while (this.update == null) {
                this.updateSyncObject.wait();
            }
        
            IMatch matchUpdate = this.update;
            this.update = null;
            return matchUpdate;
        }
    }
    
    @Override
    public void onUpdateReceived(IMatch update) {
        synchronized (this.updateSyncObject) {
            this.update = update;
        
            this.updateSyncObject.notifyAll();
        }
    }
    
    private final transient Object beginTurnSyncObject = new Object();
    private Integer timeInSeconds;
    
    public void postTurnBegin(int timeInSeconds) throws RemoteException {
        synchronized (beginTurnSyncObject) {
            this.timeInSeconds = timeInSeconds;
            beginTurnSyncObject.notifyAll();
        }
    }
    
    @Override
    public int waitForTurnToBegin() throws IOException, InterruptedException {
        //noinspection Duplicates
        synchronized (beginTurnSyncObject) {
            while (this.timeInSeconds == null) {
                beginTurnSyncObject.wait();
            }
            
            int temp = this.timeInSeconds;
            this.timeInSeconds = null;
            return temp;
        }
    }
    
    private transient Runnable endTurnRunnable;
    
    public void setEndTurnRunnable(Runnable endTurnRunnable) {
        this.endTurnRunnable = endTurnRunnable;
    }
    
    @Override
    public void endTurn() throws IOException {
        if (this.endTurnRunnable != null) {
            this.endTurnRunnable.run();
        }
    }
    
    private final transient Object endTurnSyncObject = new Object();
    private boolean turnEnded = false;
    
    public void postTurnEnd() {
        synchronized (endTurnSyncObject) {
            this.turnEnded = true;
            endTurnSyncObject.notifyAll();
        }
    }
    
    @Override
    public void waitForTurnToEnd() throws IOException, InterruptedException {
        synchronized (endTurnSyncObject) {
            while (!this.turnEnded) {
                endTurnSyncObject.wait();
            }
    
            this.turnEnded = false;
        }
    }
    
    private transient Consumer<Move> moveConsumer;
    private final transient Object moveResponseSyncObject = new Object();
    private MoveResponse moveResponse;
    
    public void setMoveFunction(Function<Move, MoveResponse> moveConsumer) {
        this.moveConsumer = move -> {
            synchronized (moveResponseSyncObject) {
                this.moveResponse = moveConsumer.apply(move);
                this.moveResponseSyncObject.notifyAll();
            }
        };
    }
    
    @Override
    public MoveResponse tryToMove(Move move) throws IOException, InterruptedException {
        if (this.moveConsumer != null) {
            this.moveConsumer.accept(move);
        }
        
        synchronized (moveResponseSyncObject) {
            while (moveResponse == null) {
                moveResponseSyncObject.wait();
            }
            
            MoveResponse temp = moveResponse;
            moveResponse = null;
            return temp;
        }
    }

    private final transient Object toolCardRequestSyncObject = new Object();
    private Response<?> toolCardResponse;
    private transient Consumer<IToolCard> toolCardConsumer;

    public void setToolCardResponse(Response<?> toolCardResponse) {
        synchronized (toolCardRequestSyncObject) {
            this.toolCardResponse = toolCardResponse;
            toolCardRequestSyncObject.notifyAll();
        }
    }

    public void setToolCardConsumer(Consumer<IToolCard> toolCardConsumer) {
        this.toolCardConsumer = toolCardConsumer;
    }
    
    @Override
    public void requestToolCardUsage(IToolCard toolCard) throws IOException, InterruptedException {
        synchronized (toolCardRequestSyncObject) {
            toolCardConsumer.accept(toolCard);

            while (toolCardResponse == null) {
                toolCardRequestSyncObject.wait();
            }

            Response<?> response = toolCardResponse;
            toolCardResponse = null;

            if (response.getBody() instanceof NotEnoughTokensResponse) {
                throw new NotEnoughTokensException(
                        ((NotEnoughTokensResponse) response.getBody()).getRequiredTokens(),
                        ((NotEnoughTokensResponse) response.getBody()).getCurrentTokens()
                );
            }
            else if (response.getBody() instanceof ConstraintNotMetResponse) {
                throw new ToolCardConditionException();
            }
        }
    }
    
    private final transient Object choosePositionSyncObject = new Object();
    private ChoosePositionForLocationRequest choosePositionRequest;
    private transient Consumer<Integer> choosePositionConsumer;
    
    public void setChoosePositionConsumer(Consumer<Integer> choosePositionConsumer) {
        this.choosePositionConsumer = choosePositionConsumer;
    }
    
    public void postChoosePositionForLocation(ChoosePositionForLocationRequest choosePositionRequest) {
        synchronized (choosePositionSyncObject) {
            this.choosePositionRequest = choosePositionRequest;
            
            choosePositionSyncObject.notifyAll();
        }
    }
    
    @Override
    public ChoosePositionForLocationRequest waitForChoosePositionFromLocation() throws InterruptedException {
        //noinspection Duplicates
        synchronized (choosePositionSyncObject) {
            while (choosePositionRequest == null) {
                choosePositionSyncObject.wait();
            }
    
            ChoosePositionForLocationRequest temp = this.choosePositionRequest;
            this.choosePositionRequest = null;
            return temp;
        }
    }
    
    @Override
    public void postChosenPosition(Integer chosenPosition) {
        choosePositionConsumer.accept(chosenPosition);
    }

    private final transient Object chooseBetweenActionsSyncObject = new Object();
    private ChooseBetweenActionsRequest chooseBetweenActionsRequest;
    private transient Consumer<IAction[]> chooseBetweenActionsConsumer;

    public void setChooseBetweenActionsConsumer(Consumer<IAction[]> chooseBetweenActionsConsumer) {
        this.chooseBetweenActionsConsumer = chooseBetweenActionsConsumer;
    }

    public void postChooseBetweenActions(ChooseBetweenActionsRequest chooseBetweenActionsRequest) {
        synchronized (chooseBetweenActionsSyncObject) {
            this.chooseBetweenActionsRequest = chooseBetweenActionsRequest;

            chooseBetweenActionsSyncObject.notifyAll();
        }
    }

    @Override
    public ChooseBetweenActionsRequest waitForChooseBetweenActions() throws InterruptedException {
        //noinspection Duplicates
        synchronized (chooseBetweenActionsSyncObject) {
            while (chooseBetweenActionsRequest == null) {
                chooseBetweenActionsSyncObject.wait();
            }

            ChooseBetweenActionsRequest temp = this.chooseBetweenActionsRequest;
            this.chooseBetweenActionsRequest = null;
            return temp;
        }
    }
    
    @Override
    public void postChosenActions(IAction[] actions) {
        if (chooseBetweenActionsConsumer != null) {
            chooseBetweenActionsConsumer.accept(actions);
        }
    }

    private final transient Object shouldRepeatSyncObject = new Object();
    private ShouldRepeatRequest shouldRepeatRequest;
    private transient Consumer<Boolean> shouldRepeatConsumer;

    public void setShouldRepeatConsumer(Consumer<Boolean> shouldRepeatConsumer) {
        this.shouldRepeatConsumer = shouldRepeatConsumer;
    }

    public void postShouldRepeat(ShouldRepeatRequest shouldRepeatRequest) {
        synchronized (shouldRepeatSyncObject) {
            this.shouldRepeatRequest = shouldRepeatRequest;

            shouldRepeatSyncObject.notifyAll();
        }
    }
    
    @Override
    public IAction waitForContinueToRepeat() throws InterruptedException {
        //noinspection Duplicates
        synchronized (shouldRepeatSyncObject) {
            while (shouldRepeatRequest == null) {
                shouldRepeatSyncObject.wait();
            }

            ShouldRepeatRequest temp = this.shouldRepeatRequest;
            this.shouldRepeatRequest = null;
            return temp.getAction();
        }
    }
    
    @Override
    public void postContinueToRepeatChoice(boolean continueToRepeat) {
        if (this.shouldRepeatConsumer != null) {
            this.shouldRepeatConsumer.accept(continueToRepeat);
        }
    }

    private final transient Object setShadeSyncObject = new Object();
    private SetShadeRequest setShadeRequest;
    private transient Consumer<Integer> setShadeConsumer;

    public void setSetShadeConsumer(Consumer<Integer> setShadeConsumer) {
        this.setShadeConsumer = setShadeConsumer;
    }

    public void postSetShade(SetShadeRequest setShadeRequest) {
        synchronized (setShadeSyncObject) {
            this.setShadeRequest = setShadeRequest;

            setShadeSyncObject.notifyAll();
        }
    }
    
    @Override
    public IDie waitForSetShade() throws InterruptedException {
        //noinspection Duplicates
        synchronized (setShadeSyncObject) {
            while (setShadeRequest == null) {
                setShadeSyncObject.wait();
            }

            SetShadeRequest temp = this.setShadeRequest;
            this.setShadeRequest = null;
            return temp.getDie();
        }
    }
    
    @Override
    public void postSetShade(Integer shade) {
        if (this.setShadeConsumer != null) {
            this.setShadeConsumer.accept(shade);
        }
    }

    private final transient Object resultsSyncObject = new Object();
    private IResult[] results;

    public void postResults(IResult[] results) {
        synchronized (resultsSyncObject) {
            this.results = results;

            resultsSyncObject.notifyAll();
        }
    }
    
    @Override
    public IResult[] waitForMatchToEnd() throws IOException, InterruptedException {
        //noinspection Duplicates
        synchronized (resultsSyncObject) {
            while (this.results == null) {
                this.resultsSyncObject.wait();
            }

            IResult[] temp = this.results;
            this.results = null;
            return temp;
        }
    }
    
    @Override
    public void close(Object... args) throws IOException {
        this.shouldBeKeptAlive = false;
    }
    
    @Override
    public Boolean onHeartBeat() throws RemoteException {
        return this.shouldBeKeptAlive;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        MatchRMIProxyController that = (MatchRMIProxyController) o;
        return Objects.equals(player, that.player);
    }
    
    @Override
    public int hashCode() {
        return this.player.hashCode();
    }
}
