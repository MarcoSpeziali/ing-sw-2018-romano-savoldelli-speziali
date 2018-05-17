package it.polimi.ingsw.compilers.instructions;

import it.polimi.ingsw.compilers.commons.CompiledParameter;
import it.polimi.ingsw.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.compilers.expressions.ExpressionCompiler;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirective;
import it.polimi.ingsw.compilers.instructions.directives.InstructionExposedVariableDirective;
import it.polimi.ingsw.compilers.instructions.predicates.CompiledPredicate;
import it.polimi.ingsw.compilers.instructions.predicates.PredicateCompiler;
import it.polimi.ingsw.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InstructionCompiler {

    private InstructionCompiler() {}

    /**
     * Compiles the instruction and its sub-instructions.
     * @param node the instruction node
     * @param instructionDirectives the instruction directives
     * @param predicateDirectives the predicate directives
     * @return the compiled instruction
     */
    public static CompiledInstruction compile(Node node, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        // gets the instruction id from the node name
        String instructionId = node.getNodeName();

        InstructionDirective directive = instructionDirectives.stream()
                .filter(instructionDirective -> instructionDirective.getId().equals(instructionId))
                .findFirst()
                .orElseThrow(() -> new UnrecognizedInstructionException(instructionId));

        // gets the instruction info
        Map<String, Object> instructionInfo = XMLUtils.xmlToMap(node);

        // gets the parameters
        List<CompiledParameter> parameters = parseParameters(instructionInfo, directive);

        // gets the exposed variables
        List<CompiledExposedVariable> exposedVariables = parseExposedVariables(instructionInfo, directive);

        return new CompiledInstruction(
                instructionId,
                directive.getTargetClass(),
                // compiles the sub instructions
                compileSubInstructions(node.getChildNodes(), instructionDirectives, predicateDirectives),
                parameters,
                exposedVariables,
                // compiles the predicates
                compilePredicates(instructionInfo, directive, predicateDirectives)
        );
    }

    /**
     * @param instructionInfo the instruction info
     * @param directive the instruction directive
     * @return the parsed parameters
     */
    private static List<CompiledParameter> parseParameters(Map<String, Object> instructionInfo, InstructionDirective directive) {
        // gets a map of paramName: directive
        Map<String, ParameterDirective> parameterDirectiveMap = directive.getParameterDirectives().stream()
                .filter(instructionParameterDirective ->
                        instructionInfo.containsKey("@" + instructionParameterDirective.getName()) &&
                                // only plain parameters are allowed, not predicates
                                !instructionParameterDirective.isPredicate()
                ).collect(Collectors.toMap(ParameterDirective::getName, o -> o));

        return instructionInfo.entrySet().stream()
                // only the attributes can be parameters
                .filter(stringObjectEntry -> stringObjectEntry.getKey().startsWith("@"))
                // and only available parameters
                .filter(stringObjectEntry ->
                        parameterDirectiveMap.containsKey(stringObjectEntry.getKey()
                                .replace("@", "")
                        )
                ).map(stringObjectEntry -> {
                    // the name of the parameter
                    String key = stringObjectEntry.getKey();
                    // the value of the parameter
                    String item = (String) stringObjectEntry.getValue();

                    // returns the parsed parameter
                    return parseParameter(
                            item,
                            parameterDirectiveMap.get(key.replace("@", ""))
                    );
                }).collect(Collectors.toList());
    }

    /**
     * @param value the value of the parameter
     * @param directive the parameter directive
     * @return the compiled parameter
     */
    private static CompiledParameter parseParameter(String value, ParameterDirective directive) {
        //noinspection unchecked
        return new CompiledParameter(
                directive.getParameterType(),
                directive.getPosition(),
                // the value gets compiled
                ExpressionCompiler.compile(value),
                directive.getName(),
                directive.getDefaultValue()
        );
    }

    /**
     * @param instructionInfo the instruction info
     * @param directive the instruction directive
     * @return the parsed exposed variables
     */
    private static List<CompiledExposedVariable> parseExposedVariables(Map<String, Object> instructionInfo, InstructionDirective directive) {
        // gets a map of exposedVariableName: directive
        Map<String, InstructionExposedVariableDirective> exposedVariableDirectiveMap =
                directive.getExposedVariableDirectives().stream()
                        .filter(instructionParameterDirective ->
                                instructionInfo.containsKey("@" + instructionParameterDirective.getName())
                        ).collect(Collectors.toMap(InstructionExposedVariableDirective::getName, o -> o));

        return instructionInfo.entrySet().stream()
                // only the attributes can be exposed variables
                .filter(stringObjectEntry -> stringObjectEntry.getKey().startsWith("@"))
                .filter(stringObjectEntry ->
                        exposedVariableDirectiveMap.containsKey(stringObjectEntry.getKey()
                                .replace("@", "")
                        )
                ).map(stringObjectEntry -> {
                    // the default name of the exposed variable
                    String key = stringObjectEntry.getKey();
                    // the override name
                    String item = (String) stringObjectEntry.getValue();

                    // returns the parsed exposed variable
                    return parseExposedVariable(
                            item,
                            exposedVariableDirectiveMap.get(key.replace("@", ""))
                    );
                }).collect(Collectors.toList());
    }

    /**
     * @param parameterValue the name that overrides the default variable name
     * @param parameterDirective the parameter directive
     * @return the compiled exposed variable
     */
    private static CompiledExposedVariable parseExposedVariable(String parameterValue, InstructionExposedVariableDirective parameterDirective) {
        return new CompiledExposedVariable(
                parameterDirective.getName(),
                parameterValue,
                parameterDirective.getVariableType()
        );
    }

    /**
     * @param children The list of children
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return the compiled sub-instructions
     */
    private static List<CompiledInstruction> compileSubInstructions(NodeList children, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        List<CompiledInstruction> subInstructions = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            // if the name of the node is #text then it is not an instruction
            if (child.getNodeName().equals("#text")) {
                continue;
            }

            try {
                // if the child is not an instruction it gets ignored
                subInstructions.add(compile(child, instructionDirectives, predicateDirectives));
            }
            catch (UnrecognizedInstructionException e) {
                // TODO: proper logging
                e.printStackTrace();
            }
        }

        return subInstructions;
    }

    /**
     * @param instructionInfo the instruction info
     * @param directive the instruction directive
     * @param predicateDirectives the predicate directives
     * @return the parsed predicates
     */
    private static List<CompiledPredicate> compilePredicates(Map<String, Object> instructionInfo, InstructionDirective directive, List<PredicateDirective> predicateDirectives) {
        // gets a map of predicateName: directive
        Map<String, ParameterDirective> parameterDirectiveMap = directive.getParameterDirectives().stream()
                .filter(instructionParameterDirective ->
                        instructionInfo.containsKey("@" + instructionParameterDirective.getName()) &&
                                // only directives are allowed
                                instructionParameterDirective.isPredicate()
                ).collect(Collectors.toMap(ParameterDirective::getName, o -> o));

        return instructionInfo.entrySet().stream()
                // only the attributes can be predicates
                .filter(stringObjectEntry -> stringObjectEntry.getKey().startsWith("@"))
                .filter(stringObjectEntry ->
                        parameterDirectiveMap.containsKey(stringObjectEntry.getKey()
                                .replace("@", "")
                        )
                ).map(stringObjectEntry -> {
                    // the predicate value
                    String item = (String) stringObjectEntry.getValue();

                    return PredicateCompiler.compile(item, predicateDirectives);
                }).collect(Collectors.toList());
    }
}
