package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.server.Constants;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLogger extends Logger {

    private static Supplier<String> loggingPath = () -> {
        String path = Paths.get(
                Constants.Paths.LOG_FOLDER.getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()) + ".log"
        ).toString();

        loggingPath = () -> path;

        return path;
    };

    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(loggingPath.get());
        } catch (IOException e) {
            getLogger(ServerLogger.class)
                    .log(Level.WARNING, "Could not create file handler at path: " + loggingPath.get(), e);
        }
    }

    /**
     * @param caller the class that uses the logger
     * @return an instance of {@link Logger}
     */
    public static Logger getLogger(Class<?> caller) {
        return new ServerLogger(caller.getName());
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
    protected ServerLogger(String name) {
        super(name, null);

        if (fileHandler != null) {
            this.addHandler(fileHandler);
        }
    }
}
