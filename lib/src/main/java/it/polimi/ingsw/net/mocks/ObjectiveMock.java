package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;

public class ObjectiveMock implements IObjective {

    @Override
    @JSONElement("points-per-completion")
    public int getPointsPerCompletion() {
        return 0;
    }
}
