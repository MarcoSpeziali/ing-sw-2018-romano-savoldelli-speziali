package it.polimi.ingsw.server.compilers.instructions.directives;

import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.server.compilers.expressions.ConstantExpressionCaster;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.server.utils.VariableSupplier;
import it.polimi.ingsw.utils.io.XMLUtils;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InstructionDirectiveCompiler {

    private InstructionDirectiveCompiler() {
    }

    /**
     * Compiles the directives to instantiate the instructions.
     *
     * @return A {@link List} of {@link InstructionDirective}
     * @throws IOException                  if any IO errors occur
     * @throws SAXException                 if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested
     */
    public static List<InstructionDirective> compile() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        return compile(Constants.Resources.INSTRUCTIONS_DIRECTIVES.getRelativePath(), true);
    }

    /**
     * Compiles the directives to instantiate the instructions.
     *
     * @param directivesPath the path to the directives
     * @param isResource     if the path refers to a resource
     * @return A list of {@link InstructionDirective}
     * @throws IOException                  if any IO errors occur
     * @throws SAXException                 if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested
     */
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    public static List<InstructionDirective> compile(String directivesPath, boolean isResource) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        // retrieves the raw directives map
        Map<String, Object>[] rawDirectives = getRawDirectives(directivesPath, isResource);

        try {
            // then the map gets "mapped" (sorry for the word pun) into the actual instructionDirective list
            return Arrays.stream(rawDirectives)
                    .map(rawDirective -> {
                        // due to the strict (and terrible, imo) exception handling of java
                        // the possible exception generated by processInstructionDirective
                        // cannot be easily propagated
                        try {
                            // processes the instruction directive
                            return processInstructionDirective(rawDirective);
                        }
                        catch (ClassNotFoundException e) {
                            return FunctionalExceptionWrapper.wrap(e);
                        }
                    }).collect(Collectors.toList());
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryFinalUnwrap(ClassNotFoundException.class);
        }
    }

    /**
     * Processes the raw directive.
     *
     * @param rawDirective The raw directive in form of a {@link Map}
     * @return The actual directive as {@link InstructionDirective}
     * @throws ClassNotFoundException if any of the class of the found parameters does not exists
     */
    @SuppressWarnings("squid:S00112") // throw new RuntimeException(e)
    private static InstructionDirective processInstructionDirective(Map<String, Object> rawDirective) throws ClassNotFoundException {
        // retrieves the id of the instruction
        String id = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_ID);

        // retrieves the class of the instruction
        String instructionClass = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_CLASS);

        // processes the raw parameters directives and maps them to the actual class
        List<InstructionParameterDirective> parametersDirectives;
        try {
            parametersDirectives =
                    Arrays.stream(getRawParametersDirectives(rawDirective))
                            .map(d -> {
                                // due to the strict (and terrible, imo) exception handling of java
                                // the possible exception generated by processInstructionParameterDirective
                                // cannot be easily propagated
                                try {
                                    return processInstructionParameterDirective(d);
                                }
                                catch (ClassNotFoundException e) {
                                    return FunctionalExceptionWrapper.wrap(e);
                                }
                            }).collect(Collectors.toList());
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryFinalUnwrap(ClassNotFoundException.class);
        }

        // processes the raw exposed variables directives and maps them to the actual class
        List<InstructionExposedVariableDirective> exposedVariableDirectives;
        try {
            exposedVariableDirectives =
                    Arrays.stream(getRawExposedVariableDirectives(rawDirective))
                            .map(d -> {
                                // due to the strict (and terrible, imo) exception handling of java
                                // the possible exception generated by processInstructionExposedVariableDirective
                                // cannot be easily propagated
                                try {
                                    return processInstructionExposedVariableDirective(d);
                                }
                                catch (ClassNotFoundException e) {
                                    return FunctionalExceptionWrapper.wrap(e);
                                }
                            }).collect(Collectors.toList());
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryFinalUnwrap(ClassNotFoundException.class);
        }

        // finally creates and returns the instruction directive
        //noinspection unchecked
        return new InstructionDirective(
                id,
                (Class<Instruction>) Class.forName(instructionClass),
                parametersDirectives,
                exposedVariableDirectives
        );
    }

    /**
     * Processes the raw parameter directive.
     *
     * @param rawDirective the raw parameter directive in form of a {@link Map}
     * @return the actual parameter directive as {@link ParameterDirective}
     * @throws ClassNotFoundException if the class of the parameter does not exists
     */
    private static InstructionParameterDirective processInstructionParameterDirective(Map<String, Object> rawDirective) throws ClassNotFoundException {
        // retrieves the class of the parameter
        String classType = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_CLASS);

        // retrieves the position of the parameter (default 0)
        String stringPosition = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_POSITION);
        int position = stringPosition == null ? 0 : Integer.parseInt(stringPosition);

        // retrieves the name of the parameter
        String name = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_NAME);

        // retrieves the default value of the parameter
        String defaultValue = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_DEFAULT_VALUE);

        // retrieves the multiplicity of the parameter
        boolean isMultiple = Boolean.parseBoolean(
                (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_IS_MULTIPLE)
        );

        // retrieves if the parameters is a predicate
        boolean isPredicate = Boolean.parseBoolean(
                (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_IS_PREDICATE)
        );

        boolean isSupplied = Boolean.parseBoolean(
                (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER_IS_SUPPLIED)
        );

        Class<? extends Serializable> parameterClass;

        if (isPredicate) {
            parameterClass = null;
        }
        else if (isSupplied) {
            parameterClass = VariableSupplier.class;
        }
        else {
            parameterClass = getClassFromName(classType, isMultiple);
        }

        // creates the parameter directive
        return new InstructionParameterDirective(
                parameterClass,
                position,
                name,
                defaultValue == null ? null : (Serializable) ConstantExpressionCaster.cast(defaultValue),
                isMultiple,
                isPredicate,
                defaultValue != null
        );
    }

    /**
     * Processes the raw exposed variable directive.
     *
     * @param rawDirective the raw exposed variable directive in form of a {@link Map}
     * @return the actual exposed variable directive as {@link InstructionExposedVariableDirective}
     * @throws ClassNotFoundException if the class of the parameter does not exists
     */
    private static InstructionExposedVariableDirective processInstructionExposedVariableDirective(Map<String, Object> rawDirective) throws ClassNotFoundException {
        // retrieves the class of the exposed variable
        String classType = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_CLASS);

        // retrieves the name of the exposed variable
        String name = (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_NAME);

        // retrieves the multiplicity of the exposed variable
        boolean isMultiple = Boolean.parseBoolean(
                (String) rawDirective.get(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_IS_MULTIPLE)
        );

        // creates the parameter directive
        return new InstructionExposedVariableDirective(
                name,
                getClassFromName(classType, isMultiple)
        );
    }

    /**
     * Reads the file and parses the xml in it into a {@link Map}.
     *
     * @param path       The path to the file, or the resource name
     * @param isResource {@code true} if the path points to a resource, {@code false} otherwise
     * @return The parsed xml as {@link Map}
     * @throws IOException                  if any IO errors occur
     * @throws SAXException                 if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested.
     */
    private static Map<String, Object>[] getRawDirectives(String path, boolean isResource) throws IOException, SAXException, ParserConfigurationException {
        Node node;

        // if the path is relative to the resources folder then the class loader must be provided
        if (isResource) {
            node = XMLUtils.parseXmlFromResource(path, InstructionDirectiveCompiler.class.getClassLoader());
        }
        else {
            node = XMLUtils.parseXml(path);
        }

        // the document
        Map<String, Object> document = XMLUtils.xmlToMap(node);

        if (document.containsKey(InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES)) {
            // the children of instructions-directives (instruction-directives)
            return XMLUtils.getMapArrayAnyway(document, InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES);
        }

        //noinspection unchecked
        return new Map[0];
    }

    /**
     * Retrieves the parameters directives from the provided instruction directives.
     *
     * @param instructionDirectives The current instruction directives
     * @return the parameters directives from the provided instruction directives
     */
    private static Map<String, Object>[] getRawParametersDirectives(Map<String, Object> instructionDirectives) {
        Map<String, Object> parameters = XMLUtils.getMap(
                instructionDirectives,
                InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETERS
        );

        if (parameters == null) {
            //noinspection unchecked
            return new Map[0];
        }

        return XMLUtils.getMapArrayAnyway(parameters, InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_PARAMETER);
    }

    /**
     * Retrieves the parameters directives from the provided instruction directives.
     *
     * @param instructionDirectives The current instruction directives
     * @return the parameters directives from the provided instruction directives
     */
    private static Map<String, Object>[] getRawExposedVariableDirectives(Map<String, Object> instructionDirectives) {
        Map<String, Object> parameters = XMLUtils.getMap(
                instructionDirectives,
                InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_EXPOSES
        );

        if (parameters == null) {
            //noinspection unchecked
            return new Map[0];
        }

        return XMLUtils.getMapArrayAnyway(parameters, InstructionDirectivesNodes.INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE);
    }

    /**
     * @param className  the original class name
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
    private final class InstructionDirectivesNodes {
        static final String INSTRUCTION_DIRECTIVES = "instruction-directives";
        static final String INSTRUCTION_DIRECTIVES_ID = "@id";
        static final String INSTRUCTION_DIRECTIVES_CLASS = "@class";
        static final String INSTRUCTION_DIRECTIVES_PARAMETERS = "parameters-directives";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER = "parameter-directives";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_CLASS = "@class";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_POSITION = "@position";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_NAME = "@name";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_DEFAULT_VALUE = "@default";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_IS_MULTIPLE = "@multiple";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_IS_PREDICATE = "@predicate";
        static final String INSTRUCTION_DIRECTIVES_PARAMETER_IS_SUPPLIED = "@supplied";
        static final String INSTRUCTION_DIRECTIVES_EXPOSES = "exposes-directives";
        static final String INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE = "variable-directives";
        static final String INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_NAME = "@name";
        static final String INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_CLASS = "@class";
        static final String INSTRUCTION_DIRECTIVES_EXPOSED_VARIABLE_IS_MULTIPLE = "@multiple";

        private InstructionDirectivesNodes() {
        }
    }
}
