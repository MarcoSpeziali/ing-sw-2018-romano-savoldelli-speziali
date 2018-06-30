package it.polimi.ingsw.core;

public final class Move {
    
    private Move() {}
    
    /**
     * The current move of the player.
     */
    private static Move currentMove;
    
    /**
     * Gets the current move of the player.
     *
     * @return the current move of the player
     */
    public static Move getCurrentMove() {
        return currentMove;
    }
    
    /**
     * Builds a new {@link Move}.
     *
     * @return a new {@link Move}
     */
    public static Move build() {
        currentMove = new Move();
        return currentMove;
    }
    
    public static Move of(int draftPoolPosition, int windowPosition) {
        Move move = new Move();
        move.draftPoolPickPosition = draftPoolPosition;
        move.windowPutPosition = windowPosition;
        
        return move;
    }
    
    private Integer draftPoolPickPosition = null;
    private Integer windowPutPosition = null;
    
    /**
     * Begins the {@link Move} by settings the position where the player picked
     * the die from the {@link it.polimi.ingsw.net.mocks.IDraftPool}.
     *
     * @param draftPoolPosition the position where the player picked the die from the {@link it.polimi.ingsw.net.mocks.IDraftPool}
     */
    public void begin(int draftPoolPosition) {
        this.draftPoolPickPosition = draftPoolPosition;
    }
    
    /**
     * Ends the {@link Move} by settings the position where the player put
     * the die in the {@link it.polimi.ingsw.net.mocks.IWindow}.
     *
     * @param windowPosition the position where the player put the die in the {@link it.polimi.ingsw.net.mocks.IWindow}
     * @return the current {@link Move}
     */
    public Move end(int windowPosition) {
        this.windowPutPosition = windowPosition;
        
        return this;
    }
    
    /**
     * Returns the position where the player picked the die from the {@link it.polimi.ingsw.net.mocks.IDraftPool}.
     *
     * @return the position where the player picked the die from the {@link it.polimi.ingsw.net.mocks.IDraftPool}
     */
    public Integer getDraftPoolPickPosition() {
        return draftPoolPickPosition;
    }
    
    /**
     * Returns the position where the player put the die in the {@link it.polimi.ingsw.net.mocks.IWindow}.
     *
     * @return the position where the player put the die in the {@link it.polimi.ingsw.net.mocks.IWindow}
     */
    public Integer getWindowPutPosition() {
        return windowPutPosition;
    }
}
