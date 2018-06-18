package it.polimi.ingsw.server;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.managers.CompilationManager;
import it.polimi.ingsw.server.managers.ThreadManager;
import it.polimi.ingsw.server.net.endpoints.LobbyEndPoint;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;
import it.polimi.ingsw.server.net.endpoints.SignUpEndPoint;
import it.polimi.ingsw.server.net.sockets.ClientAcceptor;
import it.polimi.ingsw.server.net.sockets.SocketRouter;
import it.polimi.ingsw.server.sql.SagradaDatabase;
import it.polimi.ingsw.server.utils.LoggingLevelValueConverter;
import it.polimi.ingsw.server.utils.ServerLogger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Level;

public class ServerApp {

    private static ClientAcceptor socketServer;

    public static void main(String[] args) throws IOException {
        // parses the arguments
        OptionSet options = getOptionParser().parse(args);

        try {
            // sets the log level which defaults to INFO
            if (options.has(Constants.ServerArguments.LOG_LEVEL.toString())) {
                ServerLogger.setLoggingLevel((Level) options.valueOf(Constants.ServerArguments.LOG_LEVEL.toString()));
            }
            else {
                ServerLogger.setLoggingLevel(Level.INFO);
            }

            // create the folders needed by the server
            createProjectsFolders();

            // builds the settings
            Settings.getSettings().save();

            // build the routing table for the socket router
            SocketRouter.buildRoutingTables();

            // compiles the resources (if needed)
            compileResources(options.has(
                    Constants.ServerArguments.FORCE_COMPILATION.toString()
            ));

            // checks if the database can be reached
            checkDatabaseConnection();

            // starts the multiplayer server (sockets)
            startMultiplayerServer();

            // publishes requested classes on RMI registry
            registerRMIRemoteInterfaces();

            for (; ; ) {
            }
        }
        catch (Exception e) {
            ServerLogger.getLogger().log(Level.SEVERE, "An unrecoverable error occurred: ", e);
        }
        finally {
            if (socketServer != null) {
                socketServer.close();
            }

            ServerLogger.close();
        }
    }

    /**
     * @return an {@link OptionParser} containing the accepted options
     */
    private static OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(Constants.ServerArguments.FORCE_COMPILATION.toString());
        parser.accepts(Constants.ServerArguments.LOG_LEVEL.toString())
                .withRequiredArg()
                .defaultsTo("info")
                .withValuesConvertedBy(new LoggingLevelValueConverter());

        return parser;
    }

    /**
     * Creates the folders needed by the server.
     *
     * @throws IOException if some folders could not be created
     */
    private static void createProjectsFolders() throws IOException {
        /*
         * .sagrada-server/
         *     cache/
         *         compilations/
         */
        File compilationFolder = new File(Constants.Paths.COMPILATION_FOLDER.getAbsolutePath());

        if (!compilationFolder.isDirectory() && !compilationFolder.mkdirs()) {
            throw new IOException("Could not directory structure: " + (Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()));
        }

        /*
         * .sagrada-server/
         *     logs/
         */
        File logFolder = new File(Constants.Paths.LOG_FOLDER.getAbsolutePath());

        if (!logFolder.isDirectory() && !logFolder.mkdirs()) {
            throw new IOException("Could not directory structure: " + (Constants.Paths.LOG_FOLDER.getAbsolutePath()));
        }
    }

    /**
     * Compiles the resources if needed.
     *
     * @param forceCompilation whether the compilation needs to be forces even if the compilations are up-to-date
     * @throws ClassNotFoundException       if the class of a card could not be found
     * @throws IOException                  if any IO errors occur
     * @throws SAXException                 if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested
     */
    private static void compileResources(boolean forceCompilation) throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        if (forceCompilation) {
            CompilationManager.compile(Constants.Resources.ALL);
        }
        else {
            CompilationManager.compileIfNeeded();
        }
    }

    /**
     * Starts the socket listener.
     *
     * @throws IOException if any IO errors occurs
     */
    private static void startMultiplayerServer() throws IOException {
        socketServer = new ClientAcceptor(Settings.getSettings().getSocketPort());

        Thread thread = ThreadManager.addThread(Constants.Threads.SOCKET_LISTENER, socketServer);
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Checks if the connection to the database can be made, if not the method throws a {@link SQLException}.
     *
     * @throws SQLException if the connection to the database could not be made
     */
    private static void checkDatabaseConnection() throws SQLException {
        new SagradaDatabase().close();
    }

    /**
     * Creates the {@link Registry} and the binding between the endpoints and the created {@link Registry}.
     *
     * @throws RemoteException if the reference to the {@link Registry} could not be created
     */
    private static void registerRMIRemoteInterfaces() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(Settings.getSettings().getRmiPort());

        registry.rebind(getRMIEndPointName(EndPointFunction.SIGN_IN_FULFILL_CHALLENGE), new SignInEndPoint());
        registry.rebind(getRMIEndPointName(EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION), new SignInEndPoint());
        registry.rebind(getRMIEndPointName(EndPointFunction.SIGN_UP), new SignUpEndPoint());
        registry.rebind(getRMIEndPointName(EndPointFunction.LOBBY_JOIN_REQUEST), LobbyEndPoint.getInstance());
        registry.rebind(getRMIEndPointName(EndPointFunction.LOBBY_JOIN_REQUEST_RMI), LobbyEndPoint.getInstance());
    }

    /**
     * Returns the name for the specified {@link EndPointFunction}. (//$host:$port/$endpoint)
     *
     * @param endPointFunction the {@link EndPointFunction}
     * @return the name for the specified {@link EndPointFunction}. (//$host:$port/$endpoint)
     */
    private static String getRMIEndPointName(EndPointFunction endPointFunction) {
        return String.format(
                "//%s:%d/%s",
                Settings.getSettings().getRmiHost(),
                Settings.getSettings().getRmiPort(),
                endPointFunction.toString()
        );
    }
}
