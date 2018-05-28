package it.polimi.ingsw.core;

import java.io.Serializable;

public interface IObjective extends Serializable {
    int calculatePoints(Context context);
}
