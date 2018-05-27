package it.polimi.ingsw.listeners;

import it.polimi.ingsw.models.Die;

public interface CellInteractionListener {
    void onDiePicked(Die picked);
    void onDiePut(Die put);
  }
