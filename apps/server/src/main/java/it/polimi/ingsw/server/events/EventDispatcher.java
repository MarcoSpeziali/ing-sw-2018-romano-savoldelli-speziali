package it.polimi.ingsw.server.events;

import it.polimi.ingsw.utils.ReflectionUtils;

import java.util.*;
import java.util.function.Consumer;

public class EventDispatcher {

    private EventDispatcher() {}

    private static Map<EventType, List<? super IEvent>> eventsMap = new EnumMap<>(EventType.class);

    public static synchronized <E extends IEvent> void register(E event) {
        EventType eventType = getEventTypeFromEvent(event);

        if (!eventsMap.containsKey(eventType)) {
            eventsMap.put(eventType, new LinkedList<>());
        }

        eventsMap.get(eventType).add(event);
    }

    public static synchronized <E extends IEvent> void unregister(E event) {
        EventType eventType = getEventTypeFromEvent(event);

        if (eventsMap.containsKey(eventType)) {
            eventsMap.get(eventType).remove(event);
        }
    }

    public static synchronized <E extends IEvent> void dispatch(E event, Consumer<E> eventConsumer) {
        EventType eventType = getEventTypeFromEvent(event);

        if (eventsMap.containsKey(eventType)) {
            //noinspection unchecked
            eventsMap.get(eventType).forEach(e -> eventConsumer.accept((E) e));
        }
    }

    public static synchronized <E extends IEvent> void dispatch(EventType eventType, Class<E> eventClass, Consumer<E> eventConsumer) {
        if (eventsMap.containsKey(eventType)) {
            //noinspection unchecked
            eventsMap.get(eventType).forEach(e -> eventConsumer.accept((E) e));
        }
    }

    private static EventType getEventTypeFromEvent(IEvent event) {
        Emits emitsAnnotation = event.getClass().getAnnotation(Emits.class);

        if (emitsAnnotation == null) {
            throw new IllegalArgumentException("The event must be annotated with " + Emits.class.toString());
        }

        return emitsAnnotation.value();
    }
}
