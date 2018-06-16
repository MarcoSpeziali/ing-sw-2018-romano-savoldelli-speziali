package it.polimi.ingsw.server.notifiers;

import it.polimi.ingsw.net.interfaces.updates.UpdateInterface;
import it.polimi.ingsw.server.sql.DatabasePlayer;

public interface Notifier<T extends UpdateInterface> {
    void registerPlayerForUpdates(DatabasePlayer player, T updateInterface);

    void unregisterPlayerForUpdates(DatabasePlayer player);
}
