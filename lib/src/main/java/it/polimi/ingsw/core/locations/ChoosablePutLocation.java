package it.polimi.ingsw.core;

public interface ChoosablePutLocation extends ChooseLocation, PutLocation {
    void putDie(Die die, Integer location);
}
