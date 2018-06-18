package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.application.Application;
import javafx.application.Platform;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ClientApp {
    public static void main(String[] args) {
        // parses the arguments
        OptionSet options = getOptionParser().parse(args);

        try {
            ClientLogger.setLoggingLevel(Level.FINEST);

            // create the folders needed by the server
            createProjectsFolders();

            // builds the settings
            Settings.getSettings();

            // sets the locale to the one found in the settings
            LocalizedString.invalidateCacheForNewLocale(Settings.getSettings().getLanguage().getLocale());

            if (!options.has(Constants.ClientArguments.CLI_MODE.toString())) {
                Platform.setImplicitExit(false);
                Application.launch(SagradaGUI.class, args);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ClientLogger.getLogger().log(Level.SEVERE, "An unrecoverable error occurred: ", e);
        }
    }

    /**
     * @return an {@link OptionParser} containing the accepted options
     */
    private static OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(Constants.ClientArguments.CLI_MODE.toString());

        return parser;
    }

    /**
     * Creates the folders needed by the server.
     *
     * @throws IOException if some folders could not be created
     */
    private static void createProjectsFolders() throws IOException {
        /*
         * .sagrada/
         *     logs/
         */
        File logFolder = new File(Constants.Paths.LOG_FOLDER.getAbsolutePath());

        if (!logFolder.isDirectory() && !logFolder.mkdirs()) {
            throw new IOException("Could not directory structure: " + (Constants.Paths.LOG_FOLDER.getAbsolutePath()));
        }
    }
}
