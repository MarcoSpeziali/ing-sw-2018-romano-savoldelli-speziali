package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: docs
public class ActionGroupCompiler {

    private static final String ACTION_GROUP_NODE_NAME = "action-group";
    private ActionCompiler actionCompiler;

    public CompiledActionGroup compile(Node node, List<EvaluableConstraint> constraints) throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        // This method only compiles a single <action-group ../>
        if (!node.getNodeName().equals(ACTION_GROUP_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an action-group, instead of a " + node.getNodeName());
        }

        // Gets a map representation of the provided action
        Map<String, Object> actionInfo = XmlUtils.xmlToMap(node);

        CompiledActionGroup compiledActionGroup = new CompiledActionGroup();
        compiledActionGroup.setActionData(new ActionData(
                (String) actionInfo.get("@id"),
                (String) actionInfo.get("@next"),
                (String) actionInfo.get("@description"),
                this.getActionGroupConstraint(
                        constraints,
                        (String) actionInfo.get("@constraint"),
                        (String) actionInfo.get("@id")
                ),
                null
        ));
        compiledActionGroup.setRootActionId((String) actionInfo.get("@root"));

        String repetitions = (String) actionInfo.get("@repetitions");

        if (repetitions == null) {
            compiledActionGroup.setRepetitions(IterableRange.unitaryInteger());
        }
        else {
            String[] tokens = repetitions.split("\\.\\.");

            if (tokens.length == 0) {
                // TODO: Should we throw an exception?
                compiledActionGroup.setRepetitions(IterableRange.singleValued(0, val -> ++val));
            }
            else if (tokens.length == 1) {
                Integer singleRepetition = Integer.valueOf(tokens[0]);

                compiledActionGroup.setRepetitions(IterableRange.singleValued(singleRepetition, val -> ++val));
            }
            else {
                Integer from = Integer.valueOf(tokens[0]);
                Integer to = Integer.valueOf(tokens[1]);

                compiledActionGroup.setRepetitions(new IterableRange<>(from, to, val -> ++val));
            }
        }

        String chooseBetween = (String) actionInfo.get("@chooseBetween");

        if (chooseBetween == null) {
            compiledActionGroup.setChooseBetween(IterableRange.unitaryInteger());
        }
        else {
            String[] tokens = chooseBetween.split("\\.\\.");

            if (tokens.length == 0) {
                // TODO: Should we throw an exception?
                compiledActionGroup.setChooseBetween(IterableRange.singleValued(0, val -> ++val));
            }
            else if (tokens.length == 1) {
                Integer singleChoice = Integer.valueOf(tokens[0]);

                compiledActionGroup.setChooseBetween(IterableRange.singleValued(singleChoice, val -> ++val));
            }
            else {
                Integer from = Integer.valueOf(tokens[0]);
                Integer to = Integer.valueOf(tokens[1]);

                compiledActionGroup.setChooseBetween(new IterableRange<>(from, to, val -> ++val));
            }
        }

        List<CompiledExecutableAction> subActions = new LinkedList<>();

        if (this.actionCompiler == null) {
            this.actionCompiler = new ActionCompiler();
        }

        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeName().equals("action")) {
                subActions.add(this.actionCompiler.compile(child, constraints));
            }
            else if (child.getNodeName().equals(ACTION_GROUP_NODE_NAME)) {
                subActions.add(this.compile(child, constraints));
            }
            else {
                // TODO: Throw exception
            }
        }
        
        compiledActionGroup.setActions(subActions);

        return compiledActionGroup;
    }

    private EvaluableConstraint getActionGroupConstraint(List<EvaluableConstraint> constraints, String constraintId, String actionGroupId) {
        Optional<EvaluableConstraint> targetConstraint = Optional.empty();

        if (constraints != null) {
            targetConstraint = constraints.stream()
                    .filter(constraint -> constraint.getId().equals(constraintId))
                    .findFirst();

            if (!targetConstraint.isPresent()) {
                throw new ConstraintNotFoundException(constraintId, actionGroupId);
            }
        }

        return targetConstraint.orElse(null);
    }
}
