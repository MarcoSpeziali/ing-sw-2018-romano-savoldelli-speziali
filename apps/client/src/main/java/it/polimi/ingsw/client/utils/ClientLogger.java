package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.utils.logging.LoggerBase;

import java.nio.file.Paths;
import java.util.function.Supplier;
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

    @Override
    protected String getLoggingPath() {
        return loggingPath.get();
    }

    /**
     * @param caller the class that uses the logger
     * @return an instance of {@link Logger}
     */
    public static Logger getLogger(Class<?> caller) {
        return new ClientLogger(caller.getName());
    }

    /**
     * Protected method to construct a logger for a named subsystem.
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     */
    protected ClientLogger(String name) {
        super(name);
    }
}
