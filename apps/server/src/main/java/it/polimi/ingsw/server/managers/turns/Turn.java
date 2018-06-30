package it.polimi.ingsw.server.managers.turns;

import it.polimi.ingsw.net.mocks.IPlayer;

public class Turn {
    
    public static final byte STARTED = 0b00;
    public static final byte DIE_PLACED = 0b01;
    public static final byte TOOL_CARD_USED = 0b10;
    public static final byte ENDED = DIE_PLACED | TOOL_CARD_USED;
    
    private byte phase;
    private final IPlayer player;
    private final byte round;
    private final byte turnIndex;
    
    public byte getPhase() {
        return phase;
    }
    
    public IPlayer getPlayer() {
        return player;
    }
    
    public byte getRound() {
        return round;
    }
    
    public byte getTurnIndex() {
        return turnIndex;
    }
    
    public Turn(IPlayer player, byte round, byte turnIndex) {
        this.player = player;
        this.round = round;
        this.turnIndex = turnIndex;
        
        this.phase = STARTED;
    }

    public void appendPhase(byte toAppend) {
        this.phase |= toAppend;
    }
    
    public void end() {
        synchronized (waitObject) {
            this.phase = ENDED;
            waitObject.notifyAll();
        }
    }
    
    private final Object waitObject = new Object();
    
    public void waitUntilEnded() throws InterruptedException {
        synchronized (waitObject) {
            while (this.phase != ENDED) {
                waitObject.wait();
            }
        }
    }
}
