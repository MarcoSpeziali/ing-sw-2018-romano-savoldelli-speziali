package it.polimi.ingsw.utils.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.*;

public abstract class LoggerBase extends Logger {

    /**
     * Represents the path where the logging file will be created.
     */
    protected abstract String getLoggingPath();

    protected static FileHandler fileHandler;
    private static boolean fileHandlerAlreadySet = false;
    protected static Level level;

    /**
     * Sets the logging level globally.
     * @param level the logging level to globally use
     */
    public static void setLoggingLevel(Level level) {
        LoggerBase.level = level;
    }

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     */
    @SuppressWarnings("squid:S3010")
    protected LoggerBase(String name) {
        super(name, null);

        // creates a FileHandler common for every logger
        if (!fileHandlerAlreadySet && fileHandler == null) {
            fileHandlerAlreadySet = true;

            try {
                fileHandler = new FileHandler(this.getLoggingPath());
            }
            catch (IOException e) {
                fileHandler = null;
            }
        }

        if (fileHandler != null) {
            this.addHandler(fileHandler);
            fileHandler.setLevel(level);
            fileHandler.setFormatter(new SingleLineFormatter());
        }

        this.setLevel(level);
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
