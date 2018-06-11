package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.net.commands.Handles;
import it.polimi.ingsw.server.net.commands.SignInCommands;
import it.polimi.ingsw.server.net.commands.SignUpCommand;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.JSONSerializable;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public final class SocketRouter {
    private SocketRouter() {}

    private static Map<EndPointFunction, Class<? extends Command>> routingTable;
    private static Map<EndPointFunction, Class<? extends Command>> anonymousRoutingTable;

    private static final List<Class<? extends Command>> HANDLERS = List.of(
            SignInCommands.SignInRequestCommand.class,
            SignInCommands.FulfillChallengeCommand.class,
            SignUpCommand.class
    );

    public static void buildRoutingTables() {
        routingTable = new EnumMap<>(EndPointFunction.class);
        anonymousRoutingTable = new EnumMap<>(EndPointFunction.class);

        EndPointFunction[] endPoints = EndPointFunction.values();

        for (EndPointFunction endPoint : endPoints) {
            Optional<Class<? extends Command>> optionalClass = HANDLERS.stream()
                    .filter(aClass -> aClass.getAnnotation(Handles.class).value().equals(endPoint))
                    .findFirst();

            optionalClass.ifPresent(aClass -> routingTable.put(endPoint, aClass));
        }

        for (EndPointFunction endPoint : endPoints) {
            Optional<Class<? extends Command>> optionalClass = HANDLERS.stream()
                    .filter(aClass -> {
                        Handles handles = aClass.getAnnotation(Handles.class);
                        return handles.value().equals(endPoint) && !handles.requiresAuthentication();
                    }).findFirst();

            optionalClass.ifPresent(aClass -> anonymousRoutingTable.put(endPoint, aClass));
        }
    }

    public static <R extends JSONSerializable> Command getHandlerForRequest(Request<R> request) throws SQLException {
        if (AuthenticationManager.isAuthenticated(request)) {
            return getHandlerForRequestFromRoutingTable(request, routingTable);
        }
        else {
            throw new IllegalStateException("The user must be authenticated to send this type of request: " + request.getBody().getClass());
        }
    }

    public static <R extends JSONSerializable> Command getHandlerForAnonymousRequest(Request<R> request) {
        return getHandlerForRequestFromRoutingTable(request, anonymousRoutingTable);
    }

    private static <R extends JSONSerializable> Command getHandlerForRequestFromRoutingTable(Request<R> request, Map<EndPointFunction, Class<? extends Command>> routing) {
        if (routing == null) {
            throw new IllegalStateException("The routing table has not been initialized, call SocketRouter#buildRoutingTables()");
        }

        if (routing.containsKey(request.getHeader().getEndPointFunction())) {
            Class<? extends Command> targetClass = routing.get(request.getHeader().getEndPointFunction());

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
