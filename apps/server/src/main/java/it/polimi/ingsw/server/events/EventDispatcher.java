package it.polimi.ingsw.server.events;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// TODO: docs
public class EventDispatcher {

    private static Map<EventType, List<IEvent>> eventsMap = new EnumMap<>(EventType.class);

    private EventDispatcher() {
    }

    public static synchronized <E extends IEvent> void register(E event) {
        EnumSet<EventType> eventTypes = getEventTypesFromEvent(event);
        eventTypes.forEach(eventType -> register(eventType, event));
    }

    public static synchronized void register(EventType eventType, IEvent event) {
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
        Emits[] emitsAnnotations = Arrays.stream(event.getClass().getInterfaces())
                .map(aClass -> aClass.getAnnotation(Emits.class))
                .filter(Objects::nonNull)
                .toArray(Emits[]::new);

        if (emitsAnnotations == null || emitsAnnotations.length == 0) {
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
