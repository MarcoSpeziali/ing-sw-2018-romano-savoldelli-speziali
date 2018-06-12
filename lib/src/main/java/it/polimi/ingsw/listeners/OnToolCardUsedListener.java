package it.polimi.ingsw.listeners;

import it.polimi.ingsw.models.ToolCard;

import java.io.Serializable;

public interface OnToolCardUsedListener extends Serializable {
    void onToolCardUsed(ToolCard toolCard);
}
