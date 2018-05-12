package it.polimi.ingsw.compilers.actions.directives;

import it.polimi.ingsw.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.compilers.expressions.ConstantExpressionCaster;
import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compiles the directives to instantiate the actions.
 */
public final class ActionDirectivesCompiler {

    /**
     * The default path for the actions directives.
     */
    private static final String ACTION_DIRECTIVES_PATH = "directives/actions-directives.xml";

    private ActionDirectivesCompiler() {}

    /**
     * Compiles the directives to instantiate the actions
     * @return A list of {@link ActionDirective}
     * @throws IOException If any IO errors occur
     * @throws SAXException If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<ActionDirective> compile() throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        return compile(ACTION_DIRECTIVES_PATH, true);
    }

    /**
     * Compiles the directives to instantiate the actions
     * @param directivesPath The path to the directives
     * @return A list of {@link ActionDirective}
     * @throws IOException If any IO errors occur
     * @throws SAXException If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    public static List<ActionDirective> compile(String directivesPath, boolean isResource) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        // retrieves the raw directives map
        Map<String, Object>[] rawDirectives = getRawDirectives(directivesPath, isResource);

        try {
            // then the map gets "mapped" (sorry for the word pun) into the actual ActionDirective list
            return Arrays.stream(rawDirectives)
                    .map(rawDirective -> {
                        // due to the strict (and terrible, imo) exception handling of java
                        // the possible exception generated by processActionDirective cannot
                        // be propagated easily to the compile method. A workaround is to catch
                        // the exception, rethrow it wrapped inside RuntimeException, outside
                        // the stream catch the RuntimeException and finally rethrow the original
                        // exception (ClassNotFoundException)
                        try {
                            // processes the action directive
                            return processActionDirective(rawDirective);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
        }
        catch (RuntimeException e) {
            throw (ClassNotFoundException) e.getCause();
        }
    }

    /**
     * Processes the raw directive.
     * @param rawDirective The raw directive in form of a {@link Map}
     * @return The actual directive as {@link ActionDirective}
     * @throws ClassNotFoundException if any of the class of the found parameters does not exists
     */
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    private static ActionDirective processActionDirective(Map<String, Object> rawDirective) throws ClassNotFoundException {
        // retrieves the id of the action
        String id = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_ID);

        // retrieves the class of the action
        String actionClass = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_CLASS);

        // retrieves the need for an user interaction
        boolean requiresUserInteraction = Boolean.parseBoolean(
                (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_REQUIRES_USER_INTERACTION)
        );

        // processes the raw parameters directives and maps them to the actual class
        List<ParameterDirective> parametersDirectives;
        try {
            parametersDirectives =
                    Arrays.stream(getRawParametersDirectives(rawDirective))
                            .map(d -> {
                                // due to the strict (and terrible, imo) exception handling of java
                                // the possible exception generated by processActionDirective cannot
                                // be propagated easily to the compile method. A workaround is to catch
                                // the exception, rethrow it wrapped inside RuntimeException, outside
                                // the stream catch the RuntimeException and finally rethrow the original
                                // exception (ClassNotFoundException)
                                try {
                                    return processActionParameterDirective(d);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }).collect(Collectors.toList());
        }
        catch (RuntimeException e) {
            throw (ClassNotFoundException) e.getCause();
        }

        // finally creates and returns the action directive
        //noinspection unchecked
        return new ActionDirective(
                id,
                (Class<Action>) Class.forName(actionClass),
                requiresUserInteraction,
                parametersDirectives
        );
    }

    /**
     * Processes the raw parameter directive.
     * @param rawDirective The raw parameter directive in form of a {@link Map}
     * @return The actual parameter directive as {@link ParameterDirective}
     * @throws ClassNotFoundException if the class of the parameter does not exists
     */
    private static ParameterDirective processActionParameterDirective(Map<String, Object> rawDirective) throws ClassNotFoundException {
        // retrieves the class of the parameter
        String classType = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER_CLASS);

        // retrieves the position of the parameter (default 0)
        String stringPosition = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER_POSITION);
        int position = stringPosition == null ? 0 : Integer.parseInt(stringPosition);

        // retrieves the name of the parameter
        String name = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER_NAME);

        // retrieves the default value of the parameter
        String defaultValue = (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER_DEFAULT_VALUE);

        // retrieves the multiplicity of the parameter
        boolean isMultiple = Boolean.parseBoolean(
                (String) rawDirective.get(ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER_IS_MULTIPLE)
        );

        // creates the parameter directive
        return new ParameterDirective(
                getClassFromName(classType, isMultiple),
                position,
                name,
                defaultValue == null ? null : (Serializable) ConstantExpressionCaster.cast(defaultValue),
                isMultiple
        );
    }

    /**
     * Reads the file and parses the xml in it into a {@link Map}.
     * @param path The path to the file, or the resource name
     * @param isResource {@code true} if the path points to a resource, {@code false} otherwise
     * @return The parsed xml as {@link Map}
     * @throws IOException If any IO errors occur
     * @throws SAXException If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    private static Map<String, Object>[] getRawDirectives(String path, boolean isResource) throws IOException, SAXException, ParserConfigurationException {
        Node node;

        // if the path is relative to the resources folder then the class loader must be provided
        if (isResource) {
            node = XmlUtils.parseXmlFromResource(path, ActionDirectivesCompiler.class.getClassLoader());
        }
        else {
            node = XmlUtils.parseXml(path);
        }

        // the document
        Map<String, Object> document = XmlUtils.xmlToMap(node);

        if (document.containsKey(ActionDirectivesNodes.ACTION_DIRECTIVES)) {
            // the children of actions-directives (action-directives)
            return XmlUtils.getMapArrayAnyway(document, ActionDirectivesNodes.ACTION_DIRECTIVES);
        }

        //noinspection unchecked
        return new Map[0];
    }

    /**
     * Retrieves the parameters directives from the provided action directives.
     * @param actionDirectives The current action directives
     * @return the parameters directives from the provided action directives
     */
    private static Map<String, Object>[] getRawParametersDirectives(Map<String, Object> actionDirectives) {
        Map<String, Object> parameters = XmlUtils.getMap(
                actionDirectives,
                ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETERS
        );

        return XmlUtils.getMapArrayAnyway(parameters, ActionDirectivesNodes.ACTION_DIRECTIVES_PARAMETER);
    }

    /**
     * @param className the original class name
     * @param isMultiple if the class represents an array
     * @return the array version of the provided class
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Serializable> getClassFromName(String className, boolean isMultiple) throws ClassNotFoundException {
        if (isMultiple) {
            return (Class<? extends Serializable>) Class.forName("[L" + className + ";");
        }

        return (Class<? extends Serializable>) Class.forName(className);
    }

    /**
     * Holds the constants used while accessing the xml nodes and attributes.
     */
    private final class ActionDirectivesNodes {
        static final String ACTION_DIRECTIVES = "action-directives";
        static final String ACTION_DIRECTIVES_ID = "@id";
        static final String ACTION_DIRECTIVES_CLASS = "@class";
        static final String ACTION_DIRECTIVES_REQUIRES_USER_INTERACTION = "@requiresUserInteraction";
        static final String ACTION_DIRECTIVES_PARAMETERS = "parameters-directives";
        static final String ACTION_DIRECTIVES_PARAMETER = "parameter-directives";
        static final String ACTION_DIRECTIVES_PARAMETER_CLASS = "@class";
        static final String ACTION_DIRECTIVES_PARAMETER_POSITION = "@position";
        static final String ACTION_DIRECTIVES_PARAMETER_NAME = "@name";
        static final String ACTION_DIRECTIVES_PARAMETER_DEFAULT_VALUE = "@default";
        static final String ACTION_DIRECTIVES_PARAMETER_IS_MULTIPLE = "@isMultiple";

        private ActionDirectivesNodes() { }
    }
}
