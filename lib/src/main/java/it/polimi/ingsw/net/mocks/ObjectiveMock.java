package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ObjectiveMock implements IObjective {
    
    private static final long serialVersionUID = 842836661190793531L;
    private final int pointsPerCompletion;
    
    public ObjectiveMock(IObjective iObjective) {
        this(iObjective.getPointsPerCompletion());
    }
    
    @JSONDesignatedConstructor
    public ObjectiveMock(
            @JSONElement("points-per-completion") int pointsPerCompletion
    ) {
        this.pointsPerCompletion = pointsPerCompletion;
    }
    
    @Override
    @JSONElement("points-per-completion")
    public int getPointsPerCompletion() {
        return pointsPerCompletion  ;
    }
}
