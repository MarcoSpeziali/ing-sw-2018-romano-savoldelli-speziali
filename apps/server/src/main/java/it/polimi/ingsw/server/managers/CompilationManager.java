package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.compilers.WindowCompiler;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.server.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.server.compilers.cards.CardCompiler;
import it.polimi.ingsw.server.compilers.cards.CompiledObjectiveCard;
import it.polimi.ingsw.server.compilers.cards.CompiledToolCard;
import it.polimi.ingsw.server.compilers.instructions.directives.InstructionDirective;
import it.polimi.ingsw.server.compilers.instructions.directives.InstructionDirectiveCompiler;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirectivesCompiler;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.FilesUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.LongSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CompilationManager {
    private CompilationManager() { }

    private static Logger logger = ServerLogger.getLogger(CompilationManager.class);

    /**
     * Gets the value returned by {@link #getLastCompilation()}.
     */
    private static LongSupplier lastCompilation = () -> {
        long last = getLastCompilation();
        lastCompilation = () -> last;
        return last;
    };

    /**
     * Compiles the resources even if they are up-to-date
     * @param resourcesToCompile an {@link EnumSet} of {@link Constants.Resources} to compile
     * @return a {@code long} value representing the time the last compilation occurred
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    public static long compile(Set<Constants.Resources> resourcesToCompile) throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        long compilationTime = System.currentTimeMillis();

        logger.log(Level.INFO, "Starting compilation ({0})", new Date(compilationTime));

        File compilationFolder = createCompilationDirectory(compilationTime);

        try {
            if (resourcesToCompile.contains(Constants.Resources.WINDOWS)) {
                logger.fine("Starting windows compilation");

                long start = System.currentTimeMillis();
                compileWindows(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling windows, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.WINDOWS, compilationFolder);
                logger.fine("Windows copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.ACTIONS_DIRECTIVES)) {
                logger.fine("Starting actions directives compilation");

                long start = System.currentTimeMillis();
                compileActionDirectives(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling actions directives, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.ACTIONS_DIRECTIVES, compilationFolder);
                logger.fine("Windows copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.TOOL_CARDS)) {
                logger.fine("Starting tool cards compilation");

                long start = System.currentTimeMillis();
                compileToolCards(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling tool cards, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.TOOL_CARDS, compilationFolder);
                logger.fine("Tool cards copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.PREDICATES_DIRECTIVES)) {
                logger.fine("Starting actions predicates compilation");

                long start = System.currentTimeMillis();
                compilePredicatesDirectives(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling predicates compilation, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.PREDICATES_DIRECTIVES, compilationFolder);
                logger.fine("Predicates directives copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.INSTRUCTIONS_DIRECTIVES)) {
                logger.fine("Starting actions instructions compilation");

                long start = System.currentTimeMillis();
                compileInstructionsDirectives(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling instructions directives, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.INSTRUCTIONS_DIRECTIVES, compilationFolder);
                logger.fine("Instructions directives copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.PRIVATE_CARDS)) {
                logger.fine("Starting private cards compilation");

                long start = System.currentTimeMillis();
                compilePrivateObjectiveCards(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling private cards, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.PRIVATE_CARDS, compilationFolder);
                logger.fine("Private cards copied from last compilation");
            }

            if (resourcesToCompile.contains(Constants.Resources.PUBLIC_CARDS)) {
                logger.fine("Starting public cards compilation");

                long start = System.currentTimeMillis();
                compilePublicObjectiveCards(compilationFolder);
                long end = System.currentTimeMillis();

                logger.log(Level.FINE, "Done compiling public cards, took {0}ms", end - start);
            }
            else {
                copyResource(Constants.Resources.PUBLIC_CARDS, compilationFolder);
                logger.fine("Public cards copied from last compilation");
            }
        }
        catch (Exception e) {
            FilesUtils.deleteDirectoryRecursively(compilationFolder);

            throw e;
        }

        logger.log(Level.INFO, "Done compiling resources, took: {0}ms", System.currentTimeMillis() - compilationTime);

        return compilationTime;
    }

    /**
     * Compiles the resources if needed.
     * @return a {@code long} value representing the time the last compilation occurred
     */
    public static long compileIfNeeded() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        Set<Constants.Resources> resourcesToCompile = needsRecompilation();

        if (resourcesToCompile == null) {
            logger.info("No resources need to be compiled");

            return lastCompilation.getAsLong();
        }

        Set<String> resourcesNames = resourcesToCompile.stream()
                .map(resources -> resources.toString().toLowerCase())
                .collect(Collectors.toSet());

        logger.log(Level.INFO, "The following resources need to be recompiled: {0}", String.join(
                ", ",
                resourcesNames
        ));

        return compile(resourcesToCompile);
    }

    /**
     * @return a {@link Set} of {@link Constants.Resources} containing the resources
     *         that needs recompilation
     */
    public static Set<Constants.Resources> needsRecompilation() {
        List<Constants.Resources> resources = new LinkedList<>();

        if (windowsNeedRecompilation()) {
            resources.add(Constants.Resources.WINDOWS);
        }

        if (privateCardsNeedRecompilation()) {
            resources.add(Constants.Resources.PRIVATE_CARDS);
        }

        if (publicCardsNeedRecompilation()) {
            resources.add(Constants.Resources.PUBLIC_CARDS);
        }

        if (toolCardsNeedRecompilation()) {
            resources.add(Constants.Resources.TOOL_CARDS);
        }

        if (actionsDirectivesNeedRecompilation()) {
            resources.add(Constants.Resources.ACTIONS_DIRECTIVES);
        }

        if (instructionsDirectivesNeedRecompilation()) {
            resources.add(Constants.Resources.INSTRUCTIONS_DIRECTIVES);
        }

        if (predicatesDirectivesNeedRecompilation()) {
            resources.add(Constants.Resources.PREDICATES_DIRECTIVES);
        }

        return resources.isEmpty() ? null : EnumSet.copyOf(resources);
    }

    /**
     * @return whether the windows need to be recompiled
     */
    public static boolean windowsNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.WINDOWS);
    }

    /**
     * @return whether the private cards need to be recompiled
     */
    public static boolean privateCardsNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.PRIVATE_CARDS);
    }

    /**
     * @return whether the public cards need to be recompiled
     */
    public static boolean publicCardsNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.PUBLIC_CARDS);
    }

    /**
     * @return whether the tool cards need to be recompiled
     */
    public static boolean toolCardsNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.TOOL_CARDS);
    }

    /**
     * @return whether the actions directives need to be recompiled
     */
    public static boolean actionsDirectivesNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.ACTIONS_DIRECTIVES);
    }

    /**
     * @return whether the instructions directives need to be recompiled
     */
    public static boolean instructionsDirectivesNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.INSTRUCTIONS_DIRECTIVES);
    }

    /**
     * @return whether the predicates directives need to be recompiled
     */
    public static boolean predicatesDirectivesNeedRecompilation() {
        return resourceNeedsRecompilation(Constants.Resources.PREDICATES_DIRECTIVES);
    }

    /**
     * @param resource the resource
     * @return whether the resource needs to be recompiled
     */
    private static boolean resourceNeedsRecompilation(Constants.Resources resource) {
        // gets the last modified date of the resource at the provided url
        long lastModified = FilesUtils.getLastModifiedOfFile(
                resource.getURL()
        );

        logger.log(
                Level.FINER,
                // SonarLint was complaining about this expression not being conditionally computed
                () -> String.format("Resource %s has been compiled on %s, last compilation on: %s",
                        resource.toString().toLowerCase(),
                        String.valueOf(lastModified),
                        String.valueOf(lastCompilation.getAsLong())
                )
        );

        // the resource needs to be computed only if it has been modified after the last compilation
        return lastModified > lastCompilation.getAsLong();
    }

    /**
     * @return A {@code long} value representing the time the last compilation occurred,
     *         measured in seconds since the epoch (00:00:00 GMT, January 1, 1970),
     *         or {@code 0L} no compilations occurred.
     */
    private static long getLastCompilation() {
        // gets a list containing the name of the files in the provided directory
        List<String> compilations = FilesUtils.listFilesInDirectory(
                Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()
        );

        // if none where found the minimum long value is returned
        if (compilations == null) {
            return Long.MIN_VALUE;
        }

        return compilations.stream()
                // only the files named like a timestamp are considered
                .filter(s -> s.matches("\\d+"))
                .mapToLong(Long::parseLong)
                // the max value identifies the last compilation
                .max()
                // if none where found the minimum long value is returned
                .orElse(Long.MIN_VALUE);
    }

    /**
     * Creates the folder for the new compilation.
     * @param compilationTime the time of the new compilation
     * @return the created file
     */
    private static File createCompilationDirectory(long compilationTime) throws IOException {
        File compilationFolder = new File(Paths.get(
                Constants.Paths.COMPILATION_FOLDER.getAbsolutePath(),
                String.valueOf(compilationTime)
        ).toString());
        //noinspection ResultOfMethodCallIgnored
        if (!compilationFolder.mkdir()) {
            throw new IOException("Could not create compilation directory at path: " + compilationFolder.getAbsolutePath());
        }

        return compilationFolder;
    }

    private static void copyResource(Constants.Resources resource, File compilationFolder) throws IOException {
        Files.copy(
                Paths.get(
                        Constants.Paths.COMPILATION_FOLDER.getAbsolutePath(),
                        String.valueOf(lastCompilation.getAsLong()),
                        resource.toString().toLowerCase()
                ),
                Paths.get(
                        compilationFolder.getAbsolutePath(),
                        resource.toString().toLowerCase()
                )
        );
    }

    /**
     * Compiles the windows and saves them at {@code compilationFolder}/windows.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compileWindows(File compilationFolder) throws ParserConfigurationException, SAXException, IOException {
        try {
            // compiles the windows
            List<Window> windows = WindowCompiler.compileAll();

            // and then serializes the window array info a file called windows
            serializeObjects(
                    windows.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.WINDOWS.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile windows:");
            throw e;
        }
    }

    /**
     * Compiles the tool cards and saves them at {@code compilationFolder}/tool_cards.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compileToolCards(File compilationFolder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        try {
            // deserialize the actions directives
            List<ActionDirective> actionDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.ACTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString(),
                            ActionDirective[].class
                    )
            );

            // compiles the tool cards
            List<CompiledToolCard> toolCards = CardCompiler.compileToolCards(actionDirectives);

            // and then serializes the window array info a file called windows
            serializeObjects(
                    toolCards.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.TOOL_CARDS.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile tool cards:");
            throw e;
        }
    }

    /**
     * Compiles the action directives and saves them at {@code compilationFolder}/actions_directives.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of an action directives could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compileActionDirectives(File compilationFolder) throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        try {
            // compiles the action directives
            List<ActionDirective> actionDirectives = ActionDirectivesCompiler.compile();

            // serializes them
            serializeObjects(
                    actionDirectives.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.ACTIONS_DIRECTIVES.toString().toLowerCase()
            );
        } catch (ParserConfigurationException | ClassNotFoundException | SAXException | IOException e) {
            logger.severe("Could not compile action directives:");
            throw e;
        }
    }

    /**
     * Compiles the predicates directives and saves them at {@code compilationFolder}/predicates_directives.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of an predicate directives could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compilePredicatesDirectives(File compilationFolder) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        try {
            // compiles the predicates directives
            List<PredicateDirective> predicateDirectives = PredicateDirectivesCompiler.compile();

            // and then serializes the window array info a file called windows
            serializeObjects(
                    predicateDirectives.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.PREDICATES_DIRECTIVES.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile predicates directives:");
            throw e;
        }
    }

    /**
     * Compiles the instructions directives and saves them at {@code compilationFolder}/instructions_directives.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of an instruction directives could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compileInstructionsDirectives(File compilationFolder) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        try {
            // compiles the instructions directives
            List<InstructionDirective> instructionDirectives = InstructionDirectiveCompiler.compile();

            // and then serializes the window array info a file called windows
            serializeObjects(
                    instructionDirectives.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.INSTRUCTIONS_DIRECTIVES.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile predicates directives:");
            throw e;
        }
    }

    /**
     * Compiles the private objective cards and saves them at {@code compilationFolder}/private_cards.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of a private objective card could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compilePrivateObjectiveCards(File compilationFolder) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        try {
            // deserialize the instructions directives
            List<InstructionDirective> instructionsDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.INSTRUCTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString(),
                            InstructionDirective[].class
                    )
            );

            // deserialize the predicates directives
            List<PredicateDirective> predicatesDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.PREDICATES_DIRECTIVES.toString().toLowerCase()
                            ).toString(),
                            PredicateDirective[].class
                    )
            );

            // compiles the tool cards
            List<CompiledObjectiveCard> objectiveCards = CardCompiler.compilePrivateObjectiveCards(
                    instructionsDirectives,
                    predicatesDirectives
            );

            // and then serializes the window array info a file called windows
            serializeObjects(
                    objectiveCards.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.PRIVATE_CARDS.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile private objective cards:");
            throw e;
        }
    }

    /**
     * Compiles the public objective cards and saves them at {@code compilationFolder}/public_cards.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of a public objective card could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compilePublicObjectiveCards(File compilationFolder) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        try {
            // deserialize the instructions directives
            List<InstructionDirective> instructionsDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.INSTRUCTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString(),
                            InstructionDirective[].class
                    )
            );

            // deserialize the predicates directives
            List<PredicateDirective> predicatesDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.PREDICATES_DIRECTIVES.toString().toLowerCase()
                            ).toString(),
                            PredicateDirective[].class
                    )
            );

            // compiles the tool cards
            List<CompiledObjectiveCard> objectiveCards = CardCompiler.compilePublicObjectiveCards(
                    instructionsDirectives,
                    predicatesDirectives
            );

            // and then serializes the window array info a file called windows
            serializeObjects(
                    objectiveCards.toArray(new Serializable[0]),
                    compilationFolder,
                    Constants.Resources.PUBLIC_CARDS.toString().toLowerCase()
            );
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            logger.severe("Could not compile private objective cards:");
            throw e;
        }
    }

    /**
     * Serializes an array to a file.
     * @param objects the objects to serialize
     * @param compilationFolder the folder to save the serialized objects to
     * @param resourceName the name of the file to create
     * @throws IOException if any IO exception occurs
     */
    private static <T extends Serializable> void serializeObjects(T[] objects, File compilationFolder, String resourceName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(compilationFolder, resourceName));

        try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(objects);
            out.flush();
        }
    }

    /**
     * Deserialize an array from a file.
     * @param filePath the path to the file
     * @param <T> the type of the serialized object
     * @return an array of type {@code T} containing the deserialized objects
     * @throws IOException if any IO exception occurs
     * @throws ClassNotFoundException if a class could not be found
     */
    private static <T> T[] deserializeObjects(String filePath, Class<T[]> destinationClass) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            try (ObjectInputStream in = new ObjectInputStream(fis)) {

                Serializable[] deserialized = (Serializable[]) in.readObject();

                return Arrays.copyOf(deserialized, deserialized.length, destinationClass);
            }
        }
    }
}
