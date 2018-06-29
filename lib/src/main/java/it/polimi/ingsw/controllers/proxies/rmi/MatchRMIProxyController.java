package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
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
    
    @Override
    public void waitForTurnToBegin() throws IOException {
    
    }
    
    @Override
    public void endTurn() throws IOException {
    
    }
    
    @Override
    public void waitForTurnToEnd() throws IOException {
    
    }

    public void postMoveResponse(MoveResponse moveResponse) {

    }
    
    @Override
    public MoveResponse tryToMove(Move move) throws IOException {
        return null;
    }
    
    @Override
    public void requestToolCardUsage(IToolCard toolCard) throws IOException, NotEnoughTokensException {
    
    }
    
    @Override
    public Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation() {
        return null;
    }
    
    @Override
    public void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition) {
    
    }
    
    @Override
    public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect(IEffect[] availableEffects, Range<Integer> chooseBetween) {
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
    
    @Override
    public IMatch waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
}
