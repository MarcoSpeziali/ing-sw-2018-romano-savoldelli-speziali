package it.polimi.ingsw.utils;

import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class SettingsBase {

    private static SettingsBase customSettings;

    public static SettingsBase getSettings() {
        return customSettings;
    }

    public abstract void build() throws IOException, SAXException, ParserConfigurationException;

    protected void build(Function<Map<String, String>, SettingsBase> instantiationFunction, String resourceName, String customSettingsPath, Class<? extends SettingsBase> superClass) throws IOException, SAXException, ParserConfigurationException {
        Node defaultSettingsNode = XMLUtils.parseXmlFromResource(resourceName, superClass.getClassLoader());
        Map<String, String> defaultSettingsMap = parseSettingsNode(defaultSettingsNode);

        SettingsBase defaultSettings = instantiationFunction.apply(defaultSettingsMap);

        try {
            Node customSettingsNode = XMLUtils.parseXml(customSettingsPath);

            Map<String, String> override = new HashMap<>(defaultSettingsMap);
            override.putAll(parseSettingsNode(customSettingsNode));

            customSettings = instantiationFunction.apply(override);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            customSettings = defaultSettings;
        }
    }

    private static Map<String, String> parseSettingsNode(Node node) {
        NodeList children = node.getChildNodes();

        Map<String, String> settings = new HashMap<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeName().equals("#text")) {
                continue;
            }

            settings.put(child.getNodeName(), child.getTextContent());
        }

        return settings;
    }
}
