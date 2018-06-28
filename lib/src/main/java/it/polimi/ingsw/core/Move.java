package it.polimi.ingsw.core;

public final class Move {
    
    private static Move currentMove;
    
    public static Move getCurrentMove() {
        return currentMove;
    }
    
    public static Move build() {
        currentMove = new Move();
        return currentMove;
    }
    
    private Integer draftPoolPickPosition = null;
    private Integer windowPutPosition = null;
    
    public void begin(int draftPoolPosition) {
        this.draftPoolPickPosition = draftPoolPosition;
    }
    
    public Move end(int windowPosition) {
        this.windowPutPosition = windowPosition;
        
        return this;
    }
    
    public Integer getDraftPoolPickPosition() {
        return draftPoolPickPosition;
    }
    
    public Integer getWindowPutPosition() {
        return windowPutPosition;
    }
}
