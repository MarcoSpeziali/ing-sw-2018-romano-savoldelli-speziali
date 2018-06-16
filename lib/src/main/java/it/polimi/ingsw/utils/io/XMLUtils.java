package it.polimi.ingsw.utils.io;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;


// TODO: docs 
public final class XMLUtils {
    private XMLUtils() {
    }

    public static Map<String, Object> xmlToMap(Node node) {
        Map<String, Object> hashMap = new HashMap<>();

        NamedNodeMap attributes = node.getAttributes();

        for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
            hashMap.put("@" + attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
        }

        NodeList children = node.getChildNodes();
        Map<String, List<Map<String, Object>>> temporaryChildren = new HashMap<>();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equals("#text")) {
                continue;
            }

            if (!temporaryChildren.containsKey(children.item(i).getNodeName())) {
                temporaryChildren.put(children.item(i).getNodeName(), new ArrayList<>());
            }

            temporaryChildren.get(children.item(i).getNodeName())
                    .add(xmlToMap(children.item(i)));
        }

        temporaryChildren.forEach((key, maps) -> {
            if (maps.size() == 1) {
                hashMap.put(key, maps.get(0));
            }
            else {
                hashMap.put(key, maps.toArray());
            }
        });

        hashMap.put("#text", node.getTextContent());

        return hashMap;
    }

    public static Map<String, Object> getMap(Map<String, Object> objectMap, String key) {
        //noinspection unchecked
        return (Map<String, Object>) objectMap.get(key);
    }

    public static Map<String, Object>[] getMapArray(Map<String, Object> objectMap, String key) {
        Object[] objects = (Object[]) objectMap.get(key);

        if (objects == null) {
            return null;
        }

        //noinspection unchecked
        return Arrays.stream(objects).toArray(Map[]::new);
    }

    public static Map<String, Object>[] getMapArrayAnyway(Map<String, Object> objectMap, String key) {
        Object obj = objectMap.get(key);

        if (obj == null) {
            return null;
        }

        if (obj instanceof Object[]) {
            return getMapArray(objectMap, key);
        }
        else {
            //noinspection unchecked
            return new Map[]{(Map<String, Object>) obj};
        }
    }

    /**
     * Parses the xml content of the provided file at path.
     *
     * @param path The path to the xml file.
     * @return The parsed xml document {@link Node}
     * @throws IOException                  If any IO errors occur
     * @throws SAXException                 If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested.
     */
    public static Node parseXml(String path) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        return doc.getDocumentElement();
    }

    /**
     * Parses the xml content of the provided file at path.
     *
     * @param path The path to the xml file.
     * @return The parsed xml document {@link Node}
     * @throws IOException                  If any IO errors occur
     * @throws SAXException                 If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested.
     */
    public static Node parseXml(URL path) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path.openStream());
        return doc.getDocumentElement();
    }

    /**
     * Parses the xml content of the provided resource.
     *
     * @param resourceName The name of the resource
     * @param classLoader  The {@link ClassLoader} used to get
     * @return The parsed xml document {@link Node}
     * @throws IOException                  If any IO errors occur
     * @throws SAXException                 If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested.
     */
    public static Node parseXmlFromResource(String resourceName, ClassLoader classLoader) throws ParserConfigurationException, SAXException, IOException {
        URL fileURL = Resources.getResource(classLoader, resourceName);

        if (fileURL == null) {
            throw new FileNotFoundException("The file containing the actions mapping does not exists.");
        }

        return parseXml(fileURL);
    }

    /**
     * Parses the xml content of the {@link String}.
     *
     * @param xml The xml as string
     * @return The parsed xml document {@link Node}
     * @throws IOException                  If any IO errors occur
     * @throws SAXException                 If any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *                                      cannot be created which satisfies the configuration requested.
     */
    public static Node parseXmlString(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);
        return doc.getDocumentElement();
    }
}
