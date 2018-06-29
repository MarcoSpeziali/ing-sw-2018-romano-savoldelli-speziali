package it.polimi.ingsw.server.events;

@Emits(EventType.MODEL_UPDATES)
public interface ModelUpdateListener extends IEvent {
    default void onModelUpdated(Object sender, Object model) {
    }
}
