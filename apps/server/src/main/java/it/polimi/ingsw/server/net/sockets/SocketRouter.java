package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.net.commands.Handles;
import it.polimi.ingsw.server.net.commands.SignInCommands;
import it.polimi.ingsw.server.net.commands.SignUpCommand;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public final class SocketRouter {
    private SocketRouter() {}

    private static Map<EndPointFunction, Class<? extends Command>> routingTable;

    private static final List<Class<? extends Command>> HANDLERS = List.of(
            SignInCommands.SignInRequestCommand.class,
            SignInCommands.FulfillChallengeCommand.class,
            SignUpCommand.class
    );

    public static void buildRoutingTable() {
        routingTable = new EnumMap<>(EndPointFunction.class);

        EndPointFunction[] endPoints = EndPointFunction.values();

        for (EndPointFunction endPoint : endPoints) {
            Optional<Class<? extends Command>> optionalClass = HANDLERS.stream()
                    .filter(aClass -> aClass.getAnnotation(Handles.class).value().equals(endPoint))
                    .findFirst();

            optionalClass.ifPresent(aClass -> routingTable.put(endPoint, aClass));
        }
    }

    public static Command getHandlerForRequest(Request request) {
        if (routingTable == null) {
            throw new IllegalStateException("The routing table has not been initialized, call SocketRouter#buildRoutingTable()");
        }

        if (routingTable.containsKey(request.getHeader().getEndPointFunction())) {
            Class<? extends Command> targetClass = routingTable.get(request.getHeader().getEndPointFunction());

            try {
                return targetClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                ServerLogger.getLogger(SocketRouter.class)
                        .log(
                                Level.SEVERE,
                                "Could not instantiate command handler for endpoint: " +
                                        request.getHeader().getEndPointFunction().toString(),
                                e
                        );

                return null;
            }
        }

        return null;
    }
}
