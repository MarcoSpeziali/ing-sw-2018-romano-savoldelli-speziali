package it.polimi.ingsw.utils;

import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

// TODO: docs
public abstract class SettingsBase {

    protected final String customSettingsPath;

    protected SettingsBase(String customSettingsPath) throws IllegalAccessException {
        this.customSettingsPath = customSettingsPath;

        Map<String, String> map;

        try {
            Node customSettingsNode = XMLUtils.parseXml(customSettingsPath);

            map = new HashMap<>(parseSettingsNode(customSettingsNode));
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            map = Map.of();
        }

        Setting.Builder.build(this, map);
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

    public void save() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("settings");
            doc.appendChild(rootElement);

            for (Field field : this.getClass().getDeclaredFields()) {
                Setting setting = field.getAnnotation(Setting.class);

                if (setting != null) {
                    field.setAccessible(true);

                    Element element = doc.createElement(setting.id());
                    element.appendChild(doc.createTextNode(field.get(this).toString()));
                    rootElement.appendChild(element);

                    field.setAccessible(false);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(customSettingsPath);

            transformer.transform(source, result);
        }
        catch (ParserConfigurationException | IllegalAccessException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
