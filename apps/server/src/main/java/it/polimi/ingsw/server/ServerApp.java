package it.polimi.ingsw.server;

import it.polimi.ingsw.server.managers.CompilationManager;
import it.polimi.ingsw.server.utils.ServerLogger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.logging.Level;

public class ServerApp {
    public static void main(String[] args) {
        OptionSet options = getOptionParser().parse(args);

        try {
            long compilationDirectory = compileResources(options.has(
                    Constants.ServerArgument.FORCE_COMPILATION.toString()
            ));
        } catch (Exception e) {
            ServerLogger.getLogger(ServerApp.class).log(Level.SEVERE, "An unrecoverable error occurred: ", e);
        }
    }

    /**
     * @return an {@link OptionParser} containing the accepted options
     */
    private static OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(Constants.ServerArgument.FORCE_COMPILATION.toString());

        return parser;
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
