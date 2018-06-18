package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.utils.logging.LoggerBase;

import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class ServerLogger extends LoggerBase {

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

    /**
     * Protected method to construct a logger for a named subsystem.
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     */
    protected ServerLogger() {
        super();

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(level);
        consoleHandler.setFormatter(new SingleLineFormatter());

        this.addHandler(consoleHandler);
    }

    /**
     * @return an instance of {@link Logger}
     */
    public static LoggerBase getLogger() {
        return new ServerLogger();
    }

    @Override
    protected String getLoggingPath() {
        return loggingPath.get();
    }
}
