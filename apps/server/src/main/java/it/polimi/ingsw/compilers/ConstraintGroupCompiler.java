package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.constraints.ConstraintGroup;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: docs
public class ConstraintGroupCompiler {

    private static final String CONSTRAINT_NODE_NAME = "constraint";
    private static final String CONSTRAINT_GROUP_NODE_NAME = "constraint-group";

    public static ConstraintGroup compile(Node node) {
        // This method only compiles a single <constraint-group ../>
        if (!node.getNodeName().equals(CONSTRAINT_GROUP_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to a constraint-group, instead of a " + node.getNodeName());
        }

        // Gets a map representation of the provided constraint
        Map<String, Object> constraintInfo = XmlUtils.xmlToMap(node);

        String id = (String) constraintInfo.get("@id");
        List<EvaluableConstraint> constraints = new LinkedList<>();

        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeName().equals(CONSTRAINT_NODE_NAME)) {
                constraints.add(ConstraintCompiler.compile(child));
            }
            else if (child.getNodeName().equals(CONSTRAINT_GROUP_NODE_NAME)) {
                constraints.add(ConstraintGroupCompiler.compile(child));
            }
        }

        return new ConstraintGroup(id, constraints);
    }
}
