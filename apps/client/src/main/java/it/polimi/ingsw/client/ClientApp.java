package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.ClientLogger;
import javafx.application.Application;
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

            if (!options.has(Constants.ClientArguments.CLI_MODE.toString())) {
                Application.launch(SagradaGUI.class, args);
            }
        }
        catch (Exception e) {
            ClientLogger.getLogger(ClientApp.class).log(Level.SEVERE, "An unrecoverable error occurred: ", e);
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
        if (new File(Constants.Paths.LOG_FOLDER.getAbsolutePath()).mkdirs()) {
            throw new IOException("Could directory structure: " + (Constants.Paths.LOG_FOLDER.getAbsolutePath()));
        }
    }
}
