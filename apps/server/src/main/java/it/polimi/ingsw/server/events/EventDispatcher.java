package it.polimi.ingsw.server.events;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventDispatcher {

    private EventDispatcher() {}

    private static Map<EventType, List<? super IEvent>> eventsMap = new EnumMap<>(EventType.class);

    public static synchronized <E extends IEvent> void register(E event) {
        EnumSet<EventType> eventTypes = getEventTypesFromEvent(event);
        eventTypes.forEach(eventType -> register(eventType, event));
    }

    public static synchronized <E extends IEvent> void register(EventType eventType, E event) {
        if (!eventsMap.containsKey(eventType)) {
            eventsMap.put(eventType, new LinkedList<>());
        }

        eventsMap.get(eventType).add(event);
    }

    public static synchronized <E extends IEvent> void unregister(E event) {
        EnumSet<EventType> eventTypes = getEventTypesFromEvent(event);
        eventTypes.forEach(eventType -> unregister(eventType, event));
    }

    public static synchronized <E extends IEvent> void unregister(EventType eventType, E event) {
        if (eventsMap.containsKey(eventType)) {
            eventsMap.get(eventType).remove(event);
        }
    }

    public static synchronized <E extends IEvent> void dispatch(EventType eventType, Class<E> eventClass, Consumer<E> eventConsumer) {
        dispatch(eventType, eventConsumer);
    }

    private static synchronized <E extends IEvent> void dispatch(EventType eventType, Consumer<E> eventConsumer) {
        if (eventsMap.containsKey(eventType)) {
            //noinspection unchecked
            eventsMap.get(eventType).forEach(e -> eventConsumer.accept((E) e));
        }
    }

    private static EnumSet<EventType> getEventTypesFromEvent(IEvent event) {
        Emits[] emitsAnnotations = event.getClass().getAnnotationsByType(Emits.class);

        if (emitsAnnotations == null) {
            throw new IllegalArgumentException("The event must be annotated with " + Emits.class.toString());
        }

        EnumSet<EventType> enumSet = EnumSet.noneOf(EventType.class);
        enumSet.addAll(
                Arrays.stream(emitsAnnotations)
                        .map(Emits::value)
                        .collect(Collectors.toList())
        );

        return enumSet;
    }
}
