package it.polimi.ingsw.server.managers.turns;

import it.polimi.ingsw.server.sql.DatabasePlayer;

public class Turn {
    
    public static final byte STARTED = 0b00;
    public static final byte DIE_PLACED = 0b01;
    public static final byte TOOL_CARD_USED = 0b10;
    public static final byte ENDED = DIE_PLACED | TOOL_CARD_USED;
    
    private byte phase;
    private final DatabasePlayer player;
    private final byte round;
    private final byte turnIndex;
    
    public byte getPhase() {
        return phase;
    }

    public void setPhase(byte previousPhase) {
        this.phase = previousPhase;
    }

    public DatabasePlayer getPlayer() {
        return player;
    }

    public byte getRound() {
        return round;
    }

    public byte getTurnIndex() {
        return turnIndex;
    }

    public Turn(DatabasePlayer player, byte round, byte turnIndex) {
        this.player = player;
        this.round = round;
        this.turnIndex = turnIndex;

        this.phase = STARTED;
    }

    public void appendPhase(byte toAppend) {
        synchronized (waitObject) {
            this.phase |= toAppend;

            if (this.phase == ENDED) {
                this.end();
            }
        }
    }

    public void appendTemporaryPhase(byte toAppend) {
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
