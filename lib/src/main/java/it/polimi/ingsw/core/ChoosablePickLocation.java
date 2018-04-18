package it.polimi.ingsw.core;

public interface ChoosablePickLocation extends ChooseLocation, PickLocation {
    Die pickDie(Die die);
    Die pickDie(Integer location);
}
