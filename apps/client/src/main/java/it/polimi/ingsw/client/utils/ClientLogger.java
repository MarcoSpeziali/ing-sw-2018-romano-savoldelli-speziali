package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.utils.logging.LoggerBase;

import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class ClientLogger extends LoggerBase {

    /**
     * Represents the path where the logging file will be created.
     */
    private static Supplier<String> loggingPath = () -> {
        // $HOME/.sagrada/logs/<current_time_milliseconds>
        String path = Paths.get(
                Constants.Paths.LOG_FOLDER.getAbsolutePath(),
                String.valueOf(System.currentTimeMillis())
        ).toString();

        loggingPath = () -> path;

        return path;
    };

    private static boolean logToConsole = false;

    /**
     * Protected method to construct a logger for a named subsystem.
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     */
    protected ClientLogger() {
        super();

        if (logToConsole) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(level);
            consoleHandler.setFormatter(new SingleLineFormatter());

            this.addHandler(consoleHandler);
        }
    }

    /**
     * @return an instance of {@link Logger}
     */
    public static LoggerBase getLogger() {
        return new ClientLogger();
    }

    @Override
    protected String getLoggingPath() {
        return loggingPath.get();
    }

    /**
     * Sets up the logger to log also to the console.
     */
    public static void setUpConsoleLogger() {
        logToConsole = true;
    }
}
