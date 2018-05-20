package it.polimi.ingsw.server;

import it.polimi.ingsw.server.managers.CompilationManager;
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
    public static void main(String[] args) {
        OptionSet options = getOptionParser().parse(args);

        try {
            createProjectsFolders();

            long compilationDirectory = compileResources(options.has(
                    Constants.ServerArgument.FORCE_COMPILATION.toString()
            ));
        } catch (Exception e) {
            ServerLogger.getLogger(ServerApp.class).log(Level.SEVERE, "An unrecoverable error occurred: ", e);
        }
        finally {
            ServerLogger.close();
        }
    }

    /**
     * @return an {@link OptionParser} containing the accepted options
     */
    private static OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(Constants.ServerArgument.FORCE_COMPILATION.toString());
        parser.accepts(Constants.ServerArgument.LOG_LEVEL.toString())
                .withRequiredArg()
                .defaultsTo("info")
                .withValuesConvertedBy(new LoggingLevelValueConverter());

        return parser;
    }

    /**
     * Creates the folders needed by the server
     */
    private static void createProjectsFolders() throws IOException {
        // since the compilation folder is the deepest one calling the method mkdirs create every other folders
        if (new File(Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()).mkdirs()) {
            throw new IOException("Could directory structure: " + (Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()));
        }

        if (new File(Constants.Paths.LOG_FOLDER.getAbsolutePath()).mkdirs()) {
            throw new IOException("Could directory structure: " + (Constants.Paths.LOG_FOLDER.getAbsolutePath()));
        }
    }

    private static long compileResources(boolean forceCompilation) throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        if (forceCompilation) {
            return CompilationManager.compile(Constants.Resources.ALL);
        }
        else {
            return CompilationManager.compileIfNeeded();
        }
    }
}
