package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.core.actions.ActionData;
import it.polimi.ingsw.core.actions.VariableSupplier;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class ActionCompiler {
    private ActionCompiler() {}

    private static final String ACTION_MAPPING_FILE = "factories/actions-factory.xml";

    private static Node actionsMapping;

    public static Action compile(Node node, UserInteractionProvider userInteractionProvider) {
        if (!node.getNodeName().equals("action")) {
            throw new IllegalArgumentException();
        }

        if (actionsMapping == null) {
            actionsMapping = getActionsMappingDocument();
        }

        NamedNodeMap attributes = node.getAttributes();

        String id = attributes.getNamedItem("id").getNodeValue();
        String effect = attributes.getNamedItem("effect").getNodeValue();
        String result = attributes.getNamedItem("result").getNodeValue();
        String nextId = attributes.getNamedItem("next").getNodeValue();

        String[] tokens = effect.split(" ");

        String effectId = tokens[0];

        NodeList nodes = actionsMapping.getChildNodes();
        Node targetAction = null;

        for (int i = 0; i < nodes.getLength(); i++) {
            NamedNodeMap nodeMap = nodes.item(i).getAttributes();
            if (nodeMap.getNamedItem("id").getNodeValue().equals(effectId)) {
                targetAction = nodes.item(i);
                break;
            }
        }

        NamedNodeMap targetAttributes = targetAction.getAttributes();

        String className = targetAttributes.getNamedItem("class").getNodeValue();
        String requiresUserInteraction =  targetAttributes.getNamedItem("requiresUserInteraction").getNodeValue();

        try {
            Class<?> targetClass = Class.forName(className);

            List<Class<?>> constructorParameters = new LinkedList<>();
            List<Object> instanceParameters = new LinkedList<>();

            constructorParameters.add(ActionData.class);
            instanceParameters.add(new ActionData(
                    id,
                    nextId,
                    null, // TODO: Change me
                    null, // TODO: Change me
                    result
            ));

            if (requiresUserInteraction != null && requiresUserInteraction.equals("true")) {
                constructorParameters.add(UserInteractionProvider.class);
                instanceParameters.add(userInteractionProvider);
            }

            NodeList children = targetAction.getChildNodes();
            NodeList parameters = children.item(0).getChildNodes();
            Node resultNode = children.item(1);

            for (int i = 0; i < parameters.getLength(); i++) {
                Class<?> paramClass = Class.forName(
                        parameters.item(i).getAttributes().getNamedItem("class").getNodeValue()
                );

                //constructorParameters.add(paramClass);
                String currentToken = tokens[i + 1];

                constructorParameters.add(VariableSupplier.class);
                instanceParameters.add((VariableSupplier<?>) (Context context) -> {
                    if (currentToken.startsWith("$") && currentToken.endsWith("$")) {
                        return context.get(currentToken.replace("$", ""));
                    }
                    else {
                        // FIXME: minchia
                        if (currentToken.matches("\\d+")) {
                            return Integer.getInteger(currentToken);
                        }
                        else {
                            return GlassColor.fromString(currentToken);
                        }
                    }
                });
            }

            Constructor<?> constructor = targetClass.getConstructor(
                    constructorParameters.toArray(new Class<?>[constructorParameters.size()])
            );

            return (Action) constructor.newInstance(
                    instanceParameters.toArray()
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Document getActionsMappingDocument() {
        try {
            ClassLoader classLoader = ActionCompiler.class.getClassLoader();
            File file = new File(classLoader.getResource(ACTION_MAPPING_FILE).getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
