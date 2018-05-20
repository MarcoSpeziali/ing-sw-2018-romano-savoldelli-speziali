package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.server.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.*;

public class ServerLogger extends Logger {

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

    private static FileHandler fileHandler;
    private static Level level;

    // creates a FileHandler common for every logger
    static {
        try {
            fileHandler = new FileHandler(loggingPath.get());
        } catch (IOException e) {
            // if it could not be created only the console logger will be used
            getLogger(ServerLogger.class)
                    .log(Level.WARNING, "Could not create file handler at path: " + loggingPath.get(), e);
        }
    }

    public static void setLoggingLevel(Level level) {
        level = level;
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

        // if the FileHandler could not be created only the console logger will be used
        if (fileHandler != null) {
            this.addHandler(fileHandler);
            fileHandler.setLevel(Level.FINEST);
            fileHandler.setFormatter(new SingleLineFormatter());
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(level);
        consoleHandler.setFormatter(new SingleLineFormatter());
        this.setLevel(level);

        this.addHandler(consoleHandler);
    }

    /**
     * Closes the {@link FileHandler}.
     */
    public static synchronized void close() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }

    /**
     * https://stackoverflow.com/questions/194765/how-do-i-get-java-logging-output-to-appear-on-a-single-line
     */
    public class SingleLineFormatter extends Formatter {

        private static final String FORMAT = "{0,date} {0,time}";

        Date dat = new Date();
        private MessageFormat formatter;
        private Object[] args = new Object[1];

        private String lineSeparator = "\n";

        /**
         * Format the given LogRecord.
         * @param record the log record to be formatted.
         * @return a formatted log record
         */
        public synchronized String format(LogRecord record) {

            StringBuilder sb = new StringBuilder();

            // Minimize memory allocations here.
            dat.setTime(record.getMillis());
            args[0] = dat;

            // Date and time
            StringBuffer text = new StringBuffer();
            if (formatter == null) {
                formatter = new MessageFormat(FORMAT);
            }
            formatter.format(args, text, null);
            sb.append(text);
            sb.append(" ");

            // Class name
            if (record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName());
            } else {
                sb.append(record.getLoggerName());
            }

            // Method name
            if (record.getSourceMethodName() != null) {
                sb.append(" ");
                sb.append(record.getSourceMethodName());
            }
            sb.append(" - "); // lineSeparator

            String message = formatMessage(record);

            // Level
            sb.append(record.getLevel().getLocalizedName());
            sb.append(": ");

            int iOffset = (1000 - record.getLevel().intValue()) / 100;
            for( int i = 0; i < iOffset;  i++ ){
                sb.append(" ");
            }

            sb.append(message);
            sb.append(lineSeparator);
            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                }
            }
            return sb.toString();
        }
    }
}
