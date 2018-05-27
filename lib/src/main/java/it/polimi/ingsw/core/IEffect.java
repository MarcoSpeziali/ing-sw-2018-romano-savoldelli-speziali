package it.polimi.ingsw.core;

import java.io.Serializable;

public interface IEffect extends Serializable {
    void run(String cardId);

    int getCost();

    String getDescriptionKey();
}
