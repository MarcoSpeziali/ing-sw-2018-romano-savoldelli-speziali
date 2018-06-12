package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventDispatcherTest implements LobbyEventsListener, PlayerEventsListener {

    private int playerJoinedReceived = 0;
    private int playerLeftReceived = 0;

    private int playerConnectedReceived = 0;
    private int playerDisconnectedReceived = 0;

    @BeforeEach
    void setUp() {
        playerJoinedReceived = 0;
        playerLeftReceived = 0;
        playerConnectedReceived = 0;
        playerDisconnectedReceived = 0;
    }

    @Test
    void testEventDispatcherSingleEvent() {
        EventDispatcher.register(EventType.LOBBY_EVENTS, this);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onPlayerJoined(null, null)
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(0, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onPlayerJoined(null, null)
        );

        Assertions.assertEquals(2, this.playerJoinedReceived);
        Assertions.assertEquals(0, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onPlayerLeft(null, null)
        );

        Assertions.assertEquals(2, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.unregister(EventType.LOBBY_EVENTS, this);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> {
                    listener.onPlayerLeft(null, null);
                    listener.onPlayerJoined(null, null);
                }
        );

        Assertions.assertEquals(2, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);
    }

    @Test
    void testEventDispatcherMultipleEvents() {
        EventDispatcher.register(this);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onPlayerJoined(null, null)
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(0, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onPlayerLeft(null, null)
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(0, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                PlayerEventsListener.class,
                listener -> listener.onPlayerConnected(null)
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(1, this.playerConnectedReceived);
        Assertions.assertEquals(0, this.playerDisconnectedReceived);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                PlayerEventsListener.class,
                listener -> listener.onPlayerDisconnected(null)
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(1, this.playerConnectedReceived);
        Assertions.assertEquals(1, this.playerDisconnectedReceived);

        EventDispatcher.unregister(this);

        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> {
                    listener.onPlayerJoined(null, null);
                    listener.onPlayerLeft(null, null);
                }
        );
        EventDispatcher.dispatch(
                EventType.PLAYER_EVENTS,
                PlayerEventsListener.class,
                listener -> {
                    listener.onPlayerConnected(null);
                    listener.onPlayerDisconnected(null);
                }
        );

        Assertions.assertEquals(1, this.playerJoinedReceived);
        Assertions.assertEquals(1, this.playerLeftReceived);
        Assertions.assertEquals(1, this.playerConnectedReceived);
        Assertions.assertEquals(1, this.playerDisconnectedReceived);
    }

    @Test
    void testIllegalArgumentException() {
        IEvent event = mock(IEvent.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EventDispatcher.register(event);
        });
    }

    @Override
    public void onPlayerJoined(DatabaseLobby lobby, DatabasePlayer player) {
        playerJoinedReceived++;
    }

    @Override
    public void onPlayerLeft(DatabaseLobby lobby, DatabasePlayer player) {
        playerLeftReceived++;
    }

    @Override
    public void onPlayerConnected(DatabasePlayer player) {
        playerConnectedReceived++;
    }

    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        playerDisconnectedReceived++;
    }
}