package it.polimi.ingsw.compilers.cards;

import it.polimi.ingsw.compilers.actions.directives.ActionDirective;
import it.polimi.ingsw.compilers.constraints.ConstraintCompiler;
import it.polimi.ingsw.compilers.effects.CompiledEffect;
import it.polimi.ingsw.compilers.effects.EffectCompiler;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirective;
import it.polimi.ingsw.compilers.instructions.directives.InstructionDirectiveCompiler;
import it.polimi.ingsw.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.compilers.objectives.CompiledObjective;
import it.polimi.ingsw.compilers.objectives.ObjectiveCompiler;
import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardCompiler {

    private CardCompiler() {}

    /**
     * Compiles a single tool card.
     * @param node the node identifying a single tool card
     * @return a {@link List} of {@link ToolCard}
     */
    public static CompiledToolCard compileToolCard(Node node, List<ActionDirective> actionDirectives) {
        // this method only compiles a single <card ../>
        if (!node.getNodeName().equals(CardNodes.CARD_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to a card, instead of a " + node.getNodeName());
        }

        // gets the card info
        Map<String, Object> cardInfo = XMLUtils.xmlToMap(node);

        Map<String, Object> constraintsNodeInfo = XMLUtils.getMap(cardInfo, CardNodes.CARD_CONSTRAINTS);
        List<EvaluableConstraint> constraints;

        if (constraintsNodeInfo == null) {
            constraints = List.of();
        }
        else {
            // gets the compiled constraints
            constraints = compileConstraints(
                    XMLUtils.getMapArray(
                            constraintsNodeInfo,
                            CardNodes.CARD_CONSTRAINT
                    )
            );
        }

        // returns the compiled tool card
        return new CompiledToolCard(
                compileBaseCardInfo(cardInfo),
                compileEffect(
                        getChildNamed(node, CardNodes.CARD_EFFECT),
                        actionDirectives,
                        constraints
                )
        );
    }

    /**
     * Compiles a single objective card.
     * @param node the node identifying a single tool card
     * @return a {@link List} of {@link CompiledObjectiveCard}
     */
    public static CompiledObjectiveCard compileObjectiveCard(Node node, CardVisibility visibility, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        // this method only compiles a single <card ../>
        if (!node.getNodeName().equals(CardNodes.CARD_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to a card, instead of a " + node.getNodeName());
        }

        // gets the card info
        Map<String, Object> cardInfo = XMLUtils.xmlToMap(node);

        // returns the compiled objective card
        return new CompiledObjectiveCard(
                compileBaseCardInfo(cardInfo),
                compileObjective(
                        getChildNamed(node, CardNodes.CARD_OBJECTIVE),
                        instructionDirectives,
                        predicateDirectives
                ),
                visibility
        );
    }

    /**
     * Compiles every tool card in the default file.
     * @param actionDirectives the actions directives
     * @return a {@link List} of {@link CompiledToolCard}
     */
    public static List<CompiledToolCard> compileToolCards(List<ActionDirective> actionDirectives) throws IOException, SAXException, ParserConfigurationException {
        return compileToolCards(Constants.resources.TOOL_CARDS_PATH, true, actionDirectives);
    }

    /**
     * Compiles every tool card in the file at the given path.
     * @param filePath the file path
     * @param isResource if the path is relative to the resource folder
     * @param actionDirectives the actions directives
     * @return a {@link List} of {@link CompiledToolCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<CompiledToolCard> compileToolCards(String filePath, Boolean isResource, List<ActionDirective> actionDirectives) throws ParserConfigurationException, SAXException, IOException {
        List<CompiledToolCard> compiledToolCards = new LinkedList<>();

        // gets the raw cards
        NodeList rawCards = getRawCards(filePath, isResource).getChildNodes();

        for (int i = 0; i < rawCards.getLength(); i++) {
            // the current card
            Node card = rawCards.item(i);

            // if the node is not a card it gets ignored
            try {
                compiledToolCards.add(compileToolCard(card, actionDirectives));
            }
            catch (IllegalArgumentException ignored) {
            }
        }

        return compiledToolCards;
    }

    /**
     * Compiles every private objective card in the default file.
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledObjectiveCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<CompiledObjectiveCard> compilePrivateObjectiveCards(List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) throws IOException, SAXException, ParserConfigurationException {
        return compilePrivateObjectiveCards(Constants.resources.PRIVATE_CARDS_PATH, true, instructionDirectives, predicateDirectives);
    }

    /**
     * Compiles every private objective card in the file at the given path.
     * @param filePath the file path
     * @param isResource if the path is relative to the resource folder
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledObjectiveCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<CompiledObjectiveCard> compilePrivateObjectiveCards(String filePath, Boolean isResource, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) throws ParserConfigurationException, SAXException, IOException {
        return compileObjectiveCards(filePath, isResource, CardVisibility.PRIVATE, instructionDirectives, predicateDirectives);
    }

    /**
     * Compiles every public objective card in the default file.
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledObjectiveCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<CompiledObjectiveCard> compilePublicObjectiveCards(List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) throws IOException, SAXException, ParserConfigurationException {
        return compilePublicObjectiveCards(Constants.resources.PUBLIC_CARDS_PATH, true, instructionDirectives, predicateDirectives);
    }

    /**
     * Compiles every public objective card in the file at the given path.
     * @param filePath the file path
     * @param isResource if the path is relative to the resource folder
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledObjectiveCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<CompiledObjectiveCard> compilePublicObjectiveCards(String filePath, Boolean isResource, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) throws ParserConfigurationException, SAXException, IOException {
        return compileObjectiveCards(filePath, isResource, CardVisibility.PUBLIC, instructionDirectives, predicateDirectives);
    }

    /**
     * Compiles every objective card in the file at the given path.
     * @param filePath the file path
     * @param isResource if the path is relative to the resource folder
     * @param cardVisibility the visibility of the card
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return a {@link List} of {@link CompiledObjectiveCard}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    private static List<CompiledObjectiveCard> compileObjectiveCards(String filePath, Boolean isResource, CardVisibility cardVisibility, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) throws ParserConfigurationException, SAXException, IOException {
        List<CompiledObjectiveCard> compiledObjectiveCards = new LinkedList<>();

        // gets the raw cards
        NodeList rawCards = getRawCards(filePath, isResource).getChildNodes();

        for (int i = 0; i < rawCards.getLength(); i++) {
            // the current card
            Node card = rawCards.item(i);

            // if the node is not an instruction it gets ignored
            try {
                compiledObjectiveCards.add(compileObjectiveCard(card, cardVisibility, instructionDirectives, predicateDirectives));
            }
            catch (IllegalArgumentException ignored) { }
        }

        return compiledObjectiveCards;
    }

    /**
     * Retrieves the basic card information.
     * @param cardInfo the info of the card
     * @return an instance of {@link CompiledCard} containing the basic information about the card
     */
    private static CompiledCard compileBaseCardInfo(Map<String, Object> cardInfo) {
        // gets the id of the card
        String id = (String) cardInfo.get(CardNodes.CARD_ID);

        // gets the name key of the card
        String name = (String) cardInfo.get(CardNodes.CARD_NAME);

        // returns the compiled card
        return new CompiledCard(id, name);
    }

    /**
     * Compiles the constraints.
     * @param constraintsInfo the constraints to compile
     * @return a {@link List} of {@link EvaluableConstraint}
     */
    private static List<EvaluableConstraint> compileConstraints(Map<String, Object>[] constraintsInfo) {
        if (constraintsInfo == null) {
            return List.of();
        }

        return Arrays.stream(constraintsInfo)
                .map(ConstraintCompiler::compile)
                .collect(Collectors.toList());
    }

    /**
     * @param node the node containing the effect
     * @param actionDirectives the action directives
     * @param constraints the constraints
     * @return an instance of {@link CompiledEffect}
     */
    private static CompiledEffect compileEffect(Node node, List<ActionDirective> actionDirectives, List<EvaluableConstraint> constraints) {
        return EffectCompiler.compile(node, actionDirectives, constraints);
    }

    /**
     * @param node the node containing the objective
     * @param instructionDirectives the instructions directives
     * @param predicateDirectives the predicates directives
     * @return an instance of {@link CompiledObjective}
     */
    private static CompiledObjective compileObjective(Node node, List<InstructionDirective> instructionDirectives, List<PredicateDirective> predicateDirectives) {
        return ObjectiveCompiler.compile(node, instructionDirectives, predicateDirectives);
    }

    /**
     * Reads the file and parses the xml in it into a {@link Map}.
     * @param path The path to the file, or the resource name
     * @param isResource {@code true} if the path points to a resource, {@code false} otherwise
     * @return the root node of the file
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    private static Node getRawCards(String path, boolean isResource) throws IOException, SAXException, ParserConfigurationException {
        Node node;

        // if the path is relative to the resources folder then the class loader must be provided
        if (isResource) {
            node = XMLUtils.parseXmlFromResource(path, InstructionDirectiveCompiler.class.getClassLoader());
        }
        else {
            node = XMLUtils.parseXml(path);
        }

        return node;
    }

    /**
     * @param node the parent node
     * @param nodeName the name of the target node
     * @return the child of {@code node} which name is {@code nodeName}
     */
    private static Node getChildNamed(Node node, String nodeName) {
        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName)){
                return nodeList.item(i);
            }
        }

        return null;
    }

    private class CardNodes {
        public static final String CARD_NODE_NAME = "card";
        public static final String CARD_ID = "@id";
        public static final String CARD_NAME = "@name";
        public static final String CARD_CONSTRAINTS = "constraints";
        public static final String CARD_CONSTRAINT = "constraint";
        public static final String CARD_EFFECT = "effect";
        public static final String CARD_OBJECTIVE = "objective";
    }
}
