package it.polimi.ingsw.compilers;

import it.polimi.ingsw.compilers.utils.ActionParameter;
import it.polimi.ingsw.compilers.utils.ActionParameterValue;
import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.io.XmlUtils.xmlToMap;

// TODO: docs
public class ActionCompiler {

    private static final String ACTION_MAPPING_FILE = "factories/actions-factory.xml";

    private static final String ACTION_NODE_NAME = "action";

    private Map<String, Object> actionsMapping;

    public ActionCompiler() throws ParserConfigurationException, SAXException, IOException {
        this.actionsMapping = getActionsMappingDocument();
    }

    public CompiledAction compile(Node node, List<EvaluableConstraint> constraints) throws ClassNotFoundException {
        // This method only compiles a single <action ../>
        if (!node.getNodeName().equals(ACTION_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an action, instead of a " + node.getNodeName());
        }

        // Gets a map representation of the provided action
        Map<String, Object> actionInfo = XmlUtils.xmlToMap(node);

        // Creates a RawAction objects that stores the info for the action
        RawAction rawAction = new RawAction(actionInfo);

        // Gets the action declaration from actions-factory.xml
        Map<String, Object> target = this.getTargetAction(rawAction.functionCall);

        // Gets the complete list of parameters matching the function call and the optional values
        List<ActionParameter> params = this.getActionParameters(rawAction.functionCall, target);

        // Creates the compiled action
        CompiledAction compiledAction = new CompiledAction();
        compiledAction.setActionId(rawAction.functionCall.functionId);
        //noinspection unchecked
        compiledAction.setActionClass((Class<? extends Action>) Class.forName((String) target.get("@class")));
        compiledAction.setActionData(new ActionData(
                rawAction.id,
                rawAction.nextId,
                rawAction.descriptionKey,
                this.getActionConstraint(constraints, rawAction),
                rawAction.resultIdentifier
        ));
        compiledAction.setRequiresUserInteraction(
                Boolean.parseBoolean((String) target.get("@requiresUserInteraction"))
        );
        compiledAction.setParameters(params.toArray(new ActionParameter[0]));

        return compiledAction;
    }

    private EvaluableConstraint getActionConstraint(List<EvaluableConstraint> constraints, RawAction rawAction) {
        Optional<EvaluableConstraint> targetConstraint = Optional.empty();

        if (constraints != null) {
            targetConstraint = constraints.stream()
                    .filter(constraint -> constraint.getId().equals(rawAction.constraintId))
                    .findFirst();

            if (!targetConstraint.isPresent()) {
                throw new ConstraintNotFoundException(rawAction.constraintId, rawAction.id);
            }
        }

        return targetConstraint.orElse(null);
    }

    private Map<String, Object> getTargetAction(FunctionCall fnCall) {
        Map<String, Object>[] mapping = XmlUtils.getMapArray(
                XmlUtils.getMap(actionsMapping, "actions"),
                ACTION_NODE_NAME
        );

        Optional<Map<String, Object>> optionalMap = Arrays.stream(mapping)
                .filter(stringObjectMap -> stringObjectMap.get("@id").equals(fnCall.functionId))
                .findFirst();

        if (!optionalMap.isPresent()) {
            throw new UnrecognizedActionException(fnCall.functionId);
        }

        return optionalMap.get();
    }

    private List<ActionParameter> getActionParameters(FunctionCall fnCall, Map<String, Object> target) throws ClassNotFoundException {
        Map<String, Object>[] targetParameters = XmlUtils.getMapArrayAnyway(
                XmlUtils.getMap(target, "parameters"),
                "parameter"
        );

        List<ParamTemplate> declaredParams = Arrays.stream(targetParameters)
                .map(stringObjectMap -> {
                    String positionStr = (String) stringObjectMap.get("@position");
                    Integer position = positionStr == null ? 0 : Integer.parseInt(positionStr);

                    return new ParamTemplate(
                            (String) stringObjectMap.get("@class"),
                            position,
                            (String) stringObjectMap.get("@default"),
                            (String) stringObjectMap.get("@name")
                    );
                }).sorted(Comparator.comparing(p -> p.position))
                .collect(Collectors.toList());

        List<ActionParameter> params = new ArrayList<>(declaredParams.size());

        for (int i = 0; i < declaredParams.size(); i++) {
            ParamTemplate template = declaredParams.get(i);
            ActionParameterValue paramValue;

            if (template.defaultValue == null) {
                ParameterCall temp = fnCall.parameters.get(i);
                paramValue = new ActionParameterValue(
                        temp.value.replace("$", ""),
                        temp.value.startsWith("$") && temp.value.endsWith("$")
                );
            }
            else {
                Optional<ParameterCall> optCall = fnCall.parameters.stream()
                        .filter(parameterCall -> template.name.equals(parameterCall.optionalName))
                        .findFirst();

                if (optCall.isPresent()) {
                    ParameterCall temp = optCall.get();
                    paramValue = new ActionParameterValue(
                            temp.value.replace("$", ""),
                            temp.value.startsWith("$") && temp.value.endsWith("$")
                    );
                } else {
                    // throw new UnrecognizedOptionalParameterException(rawAction.id, template.name);
                    paramValue = new ActionParameterValue(
                            template.defaultValue.replace("$", ""),
                            template.defaultValue.startsWith("$") && template.defaultValue.endsWith("$")
                    );
                }
            }

            params.add(
                    new ActionParameter(
                            Class.forName(template.type),
                            template.position,
                            paramValue,
                            template.defaultValue != null ? template.name : null,
                            template.defaultValue
                    )
            );
        }

        return params;
    }

    private static Map<String, Object> getActionsMappingDocument() throws IOException, ParserConfigurationException, SAXException {
        ClassLoader classLoader = ActionCompiler.class.getClassLoader();
        URL fileURL = classLoader.getResource(ACTION_MAPPING_FILE);

        if (fileURL == null) {
            throw new FileNotFoundException("The file containing the actions mapping does not exists.");
        }

        File file = new File(fileURL.getFile());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        return xmlToMap(doc);
    }

    private class FunctionCall {
        String functionId;
        List<ParameterCall> parameters;
    }

    private class ParameterCall {
        String optionalName;
        Boolean isOptional;
        String value;

        ParameterCall(String value, String optionalName) {
            this.isOptional = optionalName != null;
            this.value = value;
            this.optionalName = optionalName;
        }
    }

    private class ParamTemplate {
        String type;
        Integer position;
        String defaultValue;
        String name;

        ParamTemplate(String type, Integer position, String defaultValue, String name) {
            this.type = type;
            this.position = position;
            this.defaultValue = defaultValue;
            this.name = name;
        }
    }

    private class RawAction {
        String id;
        String effect;
        String resultIdentifier;
        String nextId;
        String constraintId;
        String descriptionKey;
        FunctionCall functionCall;

        RawAction(Map<String, Object> nodeDict) {
            id = (String) nodeDict.get("@id");
            effect = (String) nodeDict.get("@effect");
            resultIdentifier = (String) nodeDict.get("@result");
            nextId = (String) nodeDict.get("@next");
            constraintId = (String) nodeDict.get("@constraint");
            descriptionKey = (String) nodeDict.get("@description");

            this.createFunctionCall(this.effect);
        }

        private void createFunctionCall(String fnCall) {
            this.functionCall = new FunctionCall();

            Pattern pattern = Pattern.compile("^(?<id>\\w+)(?<params>[^\\[]+)*(\\[(?<opts>.+)])?$");
            Matcher matcher = pattern.matcher(fnCall);

            if (matcher.find()) {
                functionCall.functionId = matcher.group("id");

                String[] parameters = matcher.group("params").trim().split("\\s+");
                String optionalMatch = matcher.group("opts");

                String[] optionalParameters = optionalMatch == null ?
                        new String[0] :
                        optionalMatch.trim()
                                .replaceAll("\\s+", "")
                                .split(",");

                functionCall.parameters = new ArrayList<>(parameters.length + optionalParameters.length);

                for (String param : parameters) {
                    ParameterCall p = new ParameterCall(param, null);
                    functionCall.parameters.add(p);
                }

                for (String param : optionalParameters) {
                    String[] tokens = param.split("\\s*=\\s*");

                    String name = tokens[0];
                    String value = tokens[1];

                    ParameterCall p = new ParameterCall(value, name);
                    functionCall.parameters.add(p);
                }
            }
        }
    }
}
