package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

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
            this.windowResponseConsumer.accept(window);
            this.windowResponseConsumer = null;
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
    
    public void setMoveConsumer(Consumer<Move> moveConsumer) {
        this.moveConsumer = move -> {
            synchronized (moveResponseSyncObject) {
                moveConsumer.accept(move);
            }
        };
    }
    
    public void postMoveResponse(MoveResponse moveResponse) {
        synchronized (moveResponseSyncObject) {
            this.moveResponse = moveResponse;
            this.moveResponseSyncObject.notifyAll();
        }
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
    
    private transient Consumer<IToolCard> toolCardConsumer;
    
    public void setToolCardConsumer(Consumer<IToolCard> toolCardConsumer) {
        this.toolCardConsumer = toolCardConsumer;
    }
    
    @Override
    public void requestToolCardUsage(IToolCard toolCard) throws IOException {
        toolCardConsumer.accept(toolCard);
    }
    
    @Override
    public Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation() {
        return null;
    }
    
    @Override
    public void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition) {
    
    }

    @Override
    public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect() {
        return null;
    }
    
    @Override
    public void postChosenEffects(IEffect[] effects) {
    
    }
    
    @Override
    public IEffect waitForContinueToRepeat() {
        return null;
    }
    
    @Override
    public void postContinueToRepeatChoice(boolean continueToRepeat) {
    
    }
    
    @Override
    public IDie waitForSetShade() {
        return null;
    }
    
    @Override
    public void postSetShade(Integer shade) {
    
    }

    private final transient Object resultsSyncObject = new Object();
    private Map<IPlayer, IResult> resultMap;

    public void postResults(Map<IPlayer, IResult> resultMap) {
        synchronized (resultsSyncObject) {
            this.resultMap = resultMap;

            resultsSyncObject.notifyAll();
        }
    }
    
    @Override
    public Map<IPlayer, IResult> waitForMatchToEnd() throws IOException, InterruptedException {
        //noinspection Duplicates
        synchronized (resultsSyncObject) {
            while (this.resultMap == null) {
                this.resultsSyncObject.wait();
            }

            Map<IPlayer, IResult> temp = this.resultMap;
            this.resultMap = null;
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
