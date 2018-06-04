package it.polimi.ingsw.server;

import it.polimi.ingsw.server.constraints.Constraint;
import it.polimi.ingsw.server.managers.CompilationManager;
import it.polimi.ingsw.server.managers.ThreadManager;
import it.polimi.ingsw.server.net.SagradaMultiplayerServer;
import it.polimi.ingsw.server.utils.LoggingLevelValueConverter;
import it.polimi.ingsw.server.utils.ServerLogger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ServerApp {

    private static SagradaMultiplayerServer socketServer;

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

            // builds the custom settings (if any)
            Settings.build();

            // create the folders needed by the server
            createProjectsFolders();

            // compiles the resources (if needed)
            long compilationDirectory = compileResources(options.has(
                    Constants.ServerArguments.FORCE_COMPILATION.toString()
            ));

            // starts the multiplayer server (sockets)
            startMultiplayerServer();

            // ThreadManager.getThread(Constants.Threads.SOCKET_LISTENER).join();
        }
        catch (Exception e) {
            ServerLogger.getLogger(ServerApp.class).log(Level.SEVERE, "An unrecoverable error occurred: ", e);
        }
        finally {
            socketServer.close();
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
     * @throws IOException if some folders could not be created
     */
    private static void createProjectsFolders() throws IOException {
        /*
         * .sagrada/
         *     cache/
         *         compilations/
         */
        if (new File(Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()).mkdirs()) {
            throw new IOException("Could directory structure: " + (Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()));
        }

        /*
         * .sagrada/
         *     logs/
         */
        if (new File(Constants.Paths.LOG_FOLDER.getAbsolutePath()).mkdirs()) {
            throw new IOException("Could directory structure: " + (Constants.Paths.LOG_FOLDER.getAbsolutePath()));
        }
    }

    /**
     * Compiles the resources if needed.
     * @param forceCompilation whether the compilation needs to be forces even if the compilations are up-to-date
     * @return a {@link Long} representing the last compilation timestamp (which is also the compilation id)
     * @throws ClassNotFoundException if the class of a card could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *         cannot be created which satisfies the configuration requested
     */
    private static long compileResources(boolean forceCompilation) throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        if (forceCompilation) {
            return CompilationManager.compile(Constants.Resources.ALL);
        }
        else {
            return CompilationManager.compileIfNeeded();
        }
    }

    /**
     * Starts the socket listener.
     * @throws IOException if any IO errors occurs
     */
    private static void startMultiplayerServer() throws IOException {
        socketServer = new SagradaMultiplayerServer(Settings.getSettings().getSocketPort());

        Thread thread = ThreadManager.addThread(Constants.Threads.SOCKET_LISTENER, socketServer);
        thread.setDaemon(false);
        thread.start();
        thread.run();
    }
}
