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
     *
     * @param name A name for the logger.  This should
     *             be a dot-separated name and should normally
     *             be based on the package name or class name
     *             of the subsystem, such as java.net
     *             or javax.swing.  It may be null for anonymous Loggers.
     */
    protected ServerLogger(String name) {
        super(name);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(level);
        consoleHandler.setFormatter(new SingleLineFormatter());

        this.addHandler(consoleHandler);
    }

    /**
     * @param caller the class that uses the logger
     * @return an instance of {@link Logger}
     */
    public static LoggerBase getLogger(Class<?> caller) {
        return new ServerLogger(caller.getName());
    }

    @Override
    protected String getLoggingPath() {
        return loggingPath.get();
    }
}
