package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.net.MatchEventListener;

public interface MatchInterface {
    Match onStart(MatchEventListener listener);
}
