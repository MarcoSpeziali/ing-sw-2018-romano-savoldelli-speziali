package it.polimi.ingsw.managers;

import it.polimi.ingsw.compilers.WindowCompiler;
import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.compilers.actions.directives.ActionDirectivesCompiler;
import it.polimi.ingsw.compilers.cards.CardCompiler;
import it.polimi.ingsw.compilers.cards.CompiledObjectiveCard;
import it.polimi.ingsw.compilers.cards.CompiledToolCard;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirective;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirectiveCompiler;
import it.polimi.ingsw.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.compilers.instructions.predicates.directives.PredicateDirectivesCompiler;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.utils.io.FilesUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.logging.Logger;

public class CompilationManager {
    private CompilationManager() { }

    private static Logger logger = Logger.getGlobal();

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
    private static long compile(Set<Constants.Resources> resourcesToCompile) throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        long compilationTime = System.currentTimeMillis() / 1000;

        File compilationFolder = createCompilationDirectory(compilationTime);

        try {
            if (resourcesToCompile.contains(Constants.Resources.WINDOWS)) {
                compileWindows(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.WINDOWS, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.ACTIONS_DIRECTIVES)) {
                compileActionDirectives(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.ACTIONS_DIRECTIVES, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.TOOL_CARDS)) {
                compileToolCards(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.TOOL_CARDS, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.PREDICATES_DIRECTIVES)) {
                compilePredicatesDirectives(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.PREDICATES_DIRECTIVES, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.INSTRUCTIONS_DIRECTIVES)) {
                compileInstructionsDirectives(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.INSTRUCTIONS_DIRECTIVES, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.PRIVATE_CARDS)) {
                compilePrivateObjectiveCards(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.PRIVATE_CARDS, compilationFolder);
            }

            if (resourcesToCompile.contains(Constants.Resources.PUBLIC_CARDS)) {
                compilePublicObjectiveCards(compilationFolder);
            }
            else {
                copyResource(Constants.Resources.PUBLIC_CARDS, compilationFolder);
            }
        }
        catch (Exception e) {
            Files.delete(compilationFolder.toPath());

            throw e;
        }

        return compilationTime;
    }

    /**
     * Compiles the resources if needed.
     * @return a {@code long} value representing the time the last compilation occurred
     */
    public static long compileIfNeeded() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        Set<Constants.Resources> resourcesToCompile = needsRecompilation();

        if (resourcesToCompile == null) {
            return lastCompilation.getAsLong();
        }

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
        long lastModified = FilesUtils.getLastModifiedOfFile(
                resource.getAbsolutePath()
        );

        return lastModified > lastCompilation.getAsLong();
    }

    /**
     * @return A {@code long} value representing the time the last compilation occurred,
     *         measured in seconds since the epoch (00:00:00 GMT, January 1, 1970),
     *         or {@code 0L} no compilations occurred.
     */
    private static long getLastCompilation() {
        List<String> compilations = FilesUtils.listFilesInDirectory(
                Constants.Paths.COMPILATION_FOLDER.getAbsolutePath()
        );

        if (compilations == null) {
            return Long.MIN_VALUE;
        }

        return compilations.stream()
                .mapToLong(value -> Long.parseLong(value) / 1000)
                .max()
                .orElse(Long.MIN_VALUE);
    }

    /**
     * Creates the folder for the new compilation.
     * @param compilationTime the time of the new compilation
     * @return the created file
     */
    private static File createCompilationDirectory(long compilationTime) {
        File compilationFolder = new File(String.format(
                "%s%d", Constants.Paths.COMPILATION_FOLDER.getAbsolutePath(), compilationTime
        ));
        //noinspection ResultOfMethodCallIgnored
        compilationFolder.mkdir();

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
            List<ActionDirective> actionDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.ACTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString()
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
            List<InstructionDirective> instructionsDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.INSTRUCTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString()
                    )
            );

            List<PredicateDirective> predicatesDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.PREDICATES_DIRECTIVES.toString().toLowerCase()
                            ).toString()
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
     * Compiles the public objective cards and saves them at {@code compilationFolder}/private_cards.
     * @param compilationFolder the folder to save the compiled objects to
     * @throws ClassNotFoundException if the class of a public objective card could not be found
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested
     */
    private static void compilePublicObjectiveCards(File compilationFolder) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        try {
            List<InstructionDirective> instructionsDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.INSTRUCTIONS_DIRECTIVES.toString().toLowerCase()
                            ).toString()
                    )
            );

            List<PredicateDirective> predicatesDirectives = List.of(
                    deserializeObjects(
                            Paths.get(
                                    compilationFolder.getAbsolutePath(),
                                    Constants.Resources.PREDICATES_DIRECTIVES.toString().toLowerCase()
                            ).toString()
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
    private static void serializeObjects(Serializable[] objects, File compilationFolder, String resourceName) throws IOException {
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
    private static <T extends Serializable> T[] deserializeObjects(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                //noinspection unchecked
                return (T[]) in.readObject();
            }
        }
    }
}
