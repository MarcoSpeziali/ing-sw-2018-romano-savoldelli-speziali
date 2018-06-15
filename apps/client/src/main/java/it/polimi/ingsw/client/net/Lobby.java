package it.polimi.ingsw.client.net;

import it.polimi.ingsw.core.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    public Lobby() {}

    private List<Player> players = new ArrayList<>();
    private ObservableList<Player> observableList = FXCollections.observableList(players);

    public ObservableList<Player> getObservableList() {
        return observableList;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
